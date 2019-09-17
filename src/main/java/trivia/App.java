package trivia;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.util.*;
import java.text.SimpleDateFormat;
import org.javalite.activejdbc.Base;
import trivia.BasicAuth;

public class App {

    static User currentUser;
    static String ide;
    static List<Integer> pregHechas = new ArrayList<Integer>(); //contiene las preguntas realizadas durante la partida y en partidas anteriores
    static List<Integer> pregEnArea = new ArrayList<Integer>(); //contiene todas las preguntas posibles de realizar en un area
    static List<Integer> respHechasCorIncor = new ArrayList<Integer>(); //contiene el resultado de la pregunta hecha al jugador
    static int cantPregCorrect;
    static int cantPregIncorrect;
    static int indexPregHechas; //indica cuantas preg tenia hechas el jugador en partidas anteriores
    
    public static void main(String[] args) {

        before((request, response) -> {
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

        after((request, response) -> {
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

        //--------------------FIN GAME----------------------//
        /*


        
        //Registration User
        post("/users", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            User.createUser((String) bodyParams.get("nom"), (String) bodyParams.get("ape"), (String) bodyParams.get("dni"), (String) bodyParams.get("pass"), Integer.parseInt((String) bodyParams.get("tipoUser")));
            res.type("application/json");
            return new Gson().toJson(true);
        });

        get("/newGame", (req, res) -> { //"/newGame/:userId"
            res.type("application/json");
            return new Gson().toJson(Game.newGame(ide));//req.params("userId")
        });//FUNCIONA

        //selecciona una pregunta del area en que esta para hacer (no considera las preguntas ya hechas)
        get("/newQuestion/:areaId", (req, res) -> { //gameId/areaId
            res.type("application/json");
            List<Integer> pregHechas = Game.getAllQuestionGameArea(ide, req.params(":areaId")); //(gameId, areaId)//devuelve las preguntas hechas en el area que jugo por ultima vez
            List<Integer> pregEnArea = Game.allQuestionArea(req.params(":areaId"));
            Map pregSelec = Question.getQuestion(Integer.toString(Game.selectQuestionId(pregHechas, pregEnArea))).getCompleteQuestion();
            return new Gson().toJson(pregSelec);
        });//FUNCIONA

        get("/newAnswer/:pregId", (req, res) -> { //==> revisar xq se cambio la definicion en games
            res.type("application/json");
            return new Gson().toJson(Game.selecAnswer(req.params(":pregId")));
        });//FUNCIONA

        // returns a User by id
        get("/users/:id", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(User.getUser(req.params(":id")).getCompleteUser());
        });

        //returns all users
        get("/users", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(User.getAllUser());
        });

        //delete a User by id
        delete("/users/:id", (req, res) -> {
            res.type("application/json");
            User.deleteUser(req.params(":id"));
            return new Gson().toJson(true);
        });

        //Edit a User (put es usado para crear o editar)
        put("/users/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class
            );
            res.type("application/json");
            User toUser = User.findById(req.params(":id"));
            if (toUser != null) {
                toUser.set("nom", (String) bodyParams.get("nom"));
                toUser.set("ape", (String) bodyParams.get("ape"));
                toUser.set("dni", (String) bodyParams.get("dni"));
                toUser.set("pass", (String) bodyParams.get("pass"));
                toUser.set("tipoUser", Integer.parseInt((String) bodyParams.get("tipoUser")));
                toUser.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });

        //return todos los Games.
        get("/games", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Game.getAllGames());
        });

        //return un game de un usuario.
        get("/games/:userId", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Game.getGame(req.params(":userId")).getCompleteGame());
        });

        //agregar un nuevo game.
        post("/games", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            String f = (String) bodyParams.get("fecha");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            Game.createGame(Integer.parseInt((String) bodyParams.get("userId")), formatter.parse(f));
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //borrar un game por id
        delete("/games/:id", (req, res) -> {
            res.type("application/json");
            Game.deleteGame(req.params(":id"));
            return new Gson().toJson(true);
        });

        // returns all question
        get("/questions", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Question.getAllQuestion());
        });

        // returns a question by id
        get("/questions/:id", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Question.getQuestion(req.params(":id")).getCompleteQuestion());
        });

        //delete a question by id
        delete("/questions/:id", (req, res) -> {
            res.type("application/json");
            Question.deleteQuestion(req.params(":id"));
            return new Gson().toJson(true);
        });

        //Add question (post es usado para crear)
        post("/questions", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            Question.createQuestion((String) bodyParams.get("preg"), Integer.parseInt((String) bodyParams.get("areaId")), Integer.parseInt((String) bodyParams.get("userAdminId")));
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //Edit a User (put es usado para crear o editar)
        put("/questions/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            res.type("application/json");
            Question toQuestion = Question.findById(req.params(":id"));
            if (toQuestion != null) {
                toQuestion.set("preg", (String) bodyParams.get("preg"));
                toQuestion.set("areaId", Integer.parseInt((String) bodyParams.get("areaId")));
                toQuestion.set("userAdminId", Integer.parseInt((String) bodyParams.get("userAdminId")));
                toQuestion.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });

        // returns a answer by id
        get("/answers/:id", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Answer.getAnswer(req.params(":id")).getCompleteAnswer());
        });

        // returns all answer
        get("/answers", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Answer.getAllAnswer());
        });

        //delete a answer by id
        delete("/answers/:id", (req, res) -> {
            res.type("application/json");
            Answer.deleteAnswer(req.params(":id"));
            return new Gson().toJson(true);
        });

        //Add Answer (post es usado para crear)
        post("/answers", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            Answer.createAnswer((String) bodyParams.get("resp"), Integer.parseInt((String) bodyParams.get("tipoAnswer")), Integer.parseInt((String) bodyParams.get("pregId")));
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //Edit a User (put es usado para crear o editar)
        put("/answers/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            res.type("application/json");
            Answer toAnswer = Answer.findById(req.params(":id"));
            if (toAnswer != null) {
                toAnswer.set("resp", (String) bodyParams.get("resp"));
                toAnswer.set("tipoAnswer", Integer.parseInt((String) bodyParams.get("tipoAnswer")));
                toAnswer.set("pregId", Integer.parseInt((String) bodyParams.get("pregId")));
                toAnswer.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });*/
    }
}
