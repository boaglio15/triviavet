package trivia;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.util.*;
import org.javalite.activejdbc.Base;
import trivia.models.*;



import spark.template.mustache.MustacheTemplateEngine;
import spark.ModelAndView;
import trivia.controllers.*;

public class App {
    private static final String sessionName = "xxx";

    static User currentUser;
    static String ide;
    static List<Integer> pregHechas = new ArrayList<Integer>(); // contiene las preguntas realizadas durante la partida
                                                                // y en partidas anteriores
    static List<Integer> pregEnArea = new ArrayList<Integer>(); // contiene todas las preguntas posibles de realizar en
                                                                // un area
    static List<Integer> respHechasCorIncor = new ArrayList<Integer>(); // contiene el resultado de la pregunta hecha al
                                                                        // jugador
    static int cantPregCorrect;
    static int cantPregIncorrect;
    static int indexPregHechas; // indica cuantas preg tenia hechas el jugador en partidas anteriores
    
    

    public static void main(String[] args) {
    	staticFiles.location("/public");
    	path("/player", () -> {
	        	        
	        before("/*" ,(request, response) -> {
	            if (Base.hasConnection()) {
	                Base.close();
	            }
	            if (!Base.hasConnection()) {
	                Base.open();
	            }
	
	            String headerToken = (String) request.headers("Authorization");
	
	            if (headerToken == null
	                    || headerToken.isEmpty()
	                    || !BasicAuth.authorize(headerToken) //determina que el user y pass pasados desde la app estan en la bd
	                    ) {
	                halt(401);
	            }
	
	            currentUser = BasicAuth.getUser(headerToken); //hace visible a todo trivia el user
	            ide = Integer.toString(currentUser.getIdUser());
	        });
	
	        after("/*", (request, response) -> {
	            Base.close();
	            response.header("Access-Control-Allow-Origin", "*");
	            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
	            response.header("Access-Control-Allow-Headers",
	                    "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
	        }); 
	        	        	
	        //------------------LOGIN-------------------------//
	        post("/login", (req, res) -> {
	            res.type("application/json");
	            return currentUser.toJson(true);
	        });
	
	        //----------------REGISTRAR--------------------//
	        //Registration User
	        post("/user", (req, res) -> {
	            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
	            Double c = (Double) bodyParams.get("tipoUser");
	            int cc = c.intValue();
	            User.createUser((String) bodyParams.get("nom"), (String) bodyParams.get("ape"), (String) bodyParams.get("dni"), (String) bodyParams.get("pass"), cc);
	            int idUser = User.findUserByDni((String) bodyParams.get("dni")).getIdUser();
	            Game.createGame(idUser);
	            res.type("application/json");
	            return new Gson().toJson(true);
	        });
	
	        //---------------GAME-------------------------//
	        //inicializa partida 
	        get("/selectQuestionAnswerInit/:areaId", (req, res) -> { //:userId/:areaId
	            System.out.println(ide);
	            res.type("application/json");
	            pregHechas = Game.getAllQuestionGameArea(ide, req.params(":areaId")); //devuelve las preguntas ya hechas en el area seleccionada y las realizadas durante la partida
	            indexPregHechas = pregHechas.size();                            //det la cant de preg ya hechas
	            pregEnArea = Game.allQuestionArea(req.params(":areaId"));       //devuelve todas las preguntas que se encuentran en el area
	            cantPregCorrect = Game.getCantQuestCorrecArea(ide, pregHechas); //det la cant de preg correctas contestadas por el usuario en el area elegida
	            cantPregIncorrect = pregHechas.size() - cantPregCorrect;        //det la cant de preg incorrectas contestadas por el usuario en el area elegida
	            
	            System.out.println("USER " + ide);
	            System.out.println("AREA SELEC " + req.params(":areaId"));
	            System.out.println("PREG HECHAS INICIAL EN AREA SELEC " + pregHechas);
	            System.out.println("PREG EN AREA TOTAL INICIAL " + pregEnArea);
	            System.out.println("CANT PREG CORREC EN AREA INICIAL " + cantPregCorrect);
	            System.out.println("CANT PREG INCORREC EN AREA INICIAL " + cantPregIncorrect);
	
	            System.out.println("-----------------------");
	           
	            Map QuestionAnswerInit = Game.selectQuestionAnswerInit(req.params(":areaId"), pregHechas, pregEnArea, cantPregIncorrect); //det los datos necesarios para inicializar
	           
	            System.out.println("PREG HECHAS " + pregHechas);
	            System.out.println("PREG EN AREA " + pregEnArea);
	            return new Gson().toJson(QuestionAnswerInit);
	        });
	
	        //selecciona una pregunta para realizar (ya tiene inicializado pregHechas y pregEnArea)
	        get("/selectQuestionAnswer/:areaId", (req, res) -> { //:userId/:areaId
	            res.type("application/json");
	            System.out.println("PREG HECHAS EN AREA" + pregHechas);
	            System.out.println("PREG TOTAL EN AREA " + pregEnArea);
	            System.out.println("CANT PREG CORREC EN AREA " + cantPregCorrect);
	            System.out.println("CANT PREG INCORREC EN AREA " + cantPregIncorrect);
	            
	            Map QuestionAnswer = Game.selectQuestionAnswer(ide, pregHechas, pregEnArea, cantPregCorrect, cantPregIncorrect);
	           
	            System.out.println("PREG HECHAS EN AREA " + pregHechas);
	            System.out.println("PREG TOTAL EN AREA " + pregEnArea);
	            System.out.println("CANT PREG CORREC EN AREA " + cantPregCorrect);
	            System.out.println("CANT PREG INCORREC EN AREA " + cantPregIncorrect);
	            return new Gson().toJson(QuestionAnswer);
	
	        });
	
	        //guarda si se contesto la preg en forma correcta o incorrecta en un arreglo
	        post("/updateTypeQuest", (req, res) -> { ///:tipoResp
	            res.type("application/json");
	            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
	            Integer typeAnsw = Integer.parseInt((String) bodyParams.get("tipoResp"));//Integer.parseInt ((String) bodyParams.get("tipoResp"));
	            if (typeAnsw == 1) {
	                cantPregCorrect = cantPregCorrect + 1;  //permite llevar cuantas preg correctas tiene en la partida
	            }else{
	                cantPregIncorrect = cantPregIncorrect + 1; //permite llevar cuantas preg incorrectas tiene en la partida
	            }
	            
	            System.out.println("TIPO RESP SELECCIONADA " + typeAnsw);
	            
	            respHechasCorIncor.add(typeAnsw); //guarda el tipo de respuesta que se dio en la pregunta dada
	           
	            System.out.println("TIPO RESP HECHAS " + respHechasCorIncor);
	            System.out.println("CANT PREG CORRECT EN ESTA PARTIDA " + cantPregCorrect);
	            System.out.println("CANT PREG INCORRECT EN ESTA PARTIDA " + cantPregIncorrect);
	            return new Gson().toJson(true);
	        });
	
	        //carga las preguntas hechas en la partida y su estado en la base de datos
	        post("/exit", (req, res) -> {//:preg/:game/:est
	            res.type("application/json");
	            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
	            
	            Double a = (Double) bodyParams.get("areaId");
	            int aa = a.intValue();
	            System.out.println("AREA " + aa);
	
	            Double n = (Double) bodyParams.get("nivel");
	            int nn = n.intValue();
	            System.out.println("NIVEL " + nn);
	
	            Double c = (Double) bodyParams.get("completada");
	            int cc = c.intValue();
	            System.out.println("AREA COMPLETADA " + cc);
	
	            Integer userId = Integer.parseInt(ide);
	            System.out.println("USUARIO " + ide);
	
	            UserArea.updateAreaUser(userId, aa, cc, nn); //carga los datos de completada y nivel en caso que el usuario no este cargado 
	                                                         //o los modifica en caso que ya haya jugado
	            System.out.println("PREG EN AREA " + pregEnArea);
	            System.out.println("PREG HECHAS " + pregHechas);
	            System.out.println("TIPO RESP HECHAS " + respHechasCorIncor);
	            System.out.println("CANT PREG CORRECT " + cantPregCorrect);
	            System.out.println("INDEX PREG HECHAS INICIAL " + indexPregHechas);           
	            
	            //carga las preguntas hechas al usuario durante la partida
	            QuestionGame.updateQuestionGame(ide, indexPregHechas, pregHechas, respHechasCorIncor);
	            
	            
	            pregEnArea.clear();//reset de todas las listas y variables
	            pregHechas.clear();
	            respHechasCorIncor.clear();
	            cantPregCorrect = 0;
	            cantPregIncorrect = 0;
	            indexPregHechas = 0;
	            
	            System.out.println("PREG EN AREA " + pregEnArea);
	            System.out.println("PREG HECHAS " + pregHechas);
	            System.out.println("TIPO RESP HECHAS " + respHechasCorIncor);
	            System.out.println("CANT PREG CORRECT " + cantPregCorrect);
	            return new Gson().toJson(true);
	        });
	            
	        get("/stat/:areaId", (req, res) -> {
	            res.type("application/json");           
	            Map m = Stat.getStatPlayArea(ide, req.params(":areaId")); 
	            return new Gson().toJson(m);
	        });	  

            put("/reset", (req, res) -> {
                res.type("application/json");  
                Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
                pregEnArea.clear();//reset de todas las listas y variables
	            pregHechas.clear();
	            respHechasCorIncor.clear();
	            cantPregCorrect = 0;
	            cantPregIncorrect = 0;
	            indexPregHechas = 0;
                
                System.out.println("PREG EN AREA " + pregEnArea);
	            System.out.println("PREG HECHAS " + pregHechas);
	            System.out.println("TIPO RESP HECHAS " + respHechasCorIncor);
	            System.out.println("CANT PREG CORRECT " + cantPregCorrect);
                return new Gson().toJson(true);
            });
    	});
    	//--------------------FIN GAME----------------------//    
	        	        
    	//---------------MUSTACHE--------------------//	  
        
        
        get("/", (req, res) -> new ModelAndView(new HashMap(), "./views/home.html"), new MustacheTemplateEngine());
        
        get("/login", (req, res) -> UserControllers.login(req, res, sessionName), new MustacheTemplateEngine());
        
                
	    path("/admin", () -> {
	        
	        before("/*", (req, res) -> {
                Base.open();
            });

            after("/*", (req, res) -> {
                Base.close();
            });
            
            Map map = new HashMap();
            map.put("error", "");
                     
            get("/procesaLoginWeb", (req, res) -> UserControllers.procesaLoginWeb(req, res, sessionName), new MustacheTemplateEngine());
            
            get("/logout", (req, res) -> UserControllers.logout(req, res, sessionName));
            
            
            
	        get("/stat", (rq, rs) -> new ModelAndView(map, "./views/cargar_area.html"), new MustacheTemplateEngine());
	
			get("/procesaShowQuestInArea", StatControllers::procesaShowQuestInArea, new MustacheTemplateEngine());

			get("/procesaShowQuestCorrectArea", StatControllers::procesaShowQuestCorrectArea, new MustacheTemplateEngine());
            		
			get("/procesaShowQuestIncorrectArea", StatControllers::procesaShowQuestIncorrectArea, new MustacheTemplateEngine());

			
            //carga la vista question.html
			get("/question", (req, res) -> {
				return new ModelAndView(map, "./views/question.html");
			}, new MustacheTemplateEngine()
			);

			//crear una nueva pregunta
			post("/procesaQuestion", QuestionController::procesaQuestion, new MustacheTemplateEngine());

			//carga la vista modifyQuestion.html
			get("/modQuestion", (req, res) -> {
				return new ModelAndView(map, "./views/modifyQuestion.html");
			}, new MustacheTemplateEngine()
			);

			get("/volver", (req, res) -> {
				return new ModelAndView(map, "./views/logged.html");
			}, new MustacheTemplateEngine()
			);

			//lista las preguntas en un area
			get("/listQuestion", QuestionController::listQuestion, new MustacheTemplateEngine());

			//carga la vista putQuestion.html
			get("/putQuest", QuestionController::selectQuestion, new MustacheTemplateEngine());

			post("/mod", QuestionController::modPregunta, new MustacheTemplateEngine());
		
		});
		
	    //---------------FIN MUSTACHE-------------------------//
	        
        
    }
}
