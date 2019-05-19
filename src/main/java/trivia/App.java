package trivia;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.util.*;
import java.text.SimpleDateFormat;
import org.javalite.activejdbc.Base;

public class App {

    //static User currentUser;

    public static void main(String[] args) {

        before((request, response) -> {
            //String headerToken = (String) request.headers("users");
            //currentUser = User.getUser(headerToken);
            //User currentUser = (User) request.headers();
            //User currentUser = request.headers();
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/trivia?nullNamePatternMatchesAll=true", "root", "root");
        });

        after((request, response) -> {
            Base.close();
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers",
                    "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        });

        //------------------LOGIN-------------------------//

        get("/login/:dniUser/:pass", (req, res) -> {
            String documento = (String) req.params(":dniUser");
            String pass = (String) req.params(":pass");
            int idUsr = User.userLogin(documento, pass);
            if (idUsr > 0)
                return new Gson().toJson(true);
            return new Gson().toJson(false);
        });

        //Registration User
        post("/users", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            String nombre = (String) bodyParams.get("nom");
            String apellido = (String) bodyParams.get("ape");
            String dni = (String) bodyParams.get("dni");
            String password = (String) bodyParams.get("pass");
            int tipo = Integer.parseInt((String) bodyParams.get("tipoUser"));
            int nivel = Integer.parseInt((String) bodyParams.get("nivel"));
            User.createUser(nombre, apellido, dni, password, tipo, nivel);
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //-------------------------------FIN LOGIN---------------------------------------//

        //supongo que el usuario se loguea por primera vez y esto automaticamente crea un juego
        //(si ya esta loguado y quiere jugar nuevamente hay que buscar su juego iniciado)
        // inicio juego
        //devuelve datos de la ultima partida del usuario (nivel y areas en las que esta jugando)
        get("/newGame/:userId", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Game.newGame(req.params(":userId")));
        });//FUNCIONA

        //selecciona una pregunta del area en que esta para hacer (no considera las preguntas ya hechas)
        get("/newQuestion/:gameId/:areaId", (req, res) -> {
            res.type("application/json");
            List<Integer> pregHechas = Game.getAllQuestionGameArea(req.params(":gameId"), req.params(":areaId")); //(gameId, areaId)//devuelve las preguntas hechas en el area que jugo por ultima vez
            List<Integer> pregEnArea = Game.allQuestionArea(req.params(":areaId"));
            String pregSelec = Question.getQuestion(Integer.toString(Game.selectQuestion(pregHechas, pregEnArea))).getPreg();
            return new Gson().toJson(pregSelec);
        });//FUNCIONA

        get("/newAnswer/:pregId", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Game.selecAnswer(req.params(":pregId")));
        });//FUNCIONA

        //fin juego
        //-----------------------------------------USER---------------------//
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
            String id = req.params(":id");
            String nombre = (String) bodyParams.get("nom");
            String apellido = (String) bodyParams.get("ape");
            String dni = (String) bodyParams.get("dni");
            String password = (String) bodyParams.get("pass");
            Integer tipo = Integer.parseInt((String) bodyParams.get("tipoUser"));
            Integer nivel = Integer.parseInt((String) bodyParams.get("nivel"));
            User toUser = User.findById(id);
            if (toUser != null) {
                toUser.set("nom", nombre);
                toUser.set("ape", apellido);
                toUser.set("dni", dni);
                toUser.set("pass", password);
                toUser.set("tipoUser", tipo);
                toUser.set("nivel", nivel);
                toUser.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });

        //----------------------------------------END USER--------------//
        //
        //-----------------------------------GAME-------------------//
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
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class
            );
            Integer usuarioId = Integer.parseInt((String) bodyParams.get("userId"));
            String f = (String) bodyParams.get("fecha");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            Date gameFecha = formatter.parse(f);
            Game.createGame(usuarioId, gameFecha);
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //borrar un game por id
        delete("/games/:id", (req, res) -> {
            res.type("application/json");
            Game.deleteGame(req.params(":id"));
            return new Gson().toJson(true);
        });
        //----------------------------------------END GAME----------//
        //
        //-----------------------------QUESTION--------------//
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
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class
            ); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            String preg = (String) bodyParams.get("preg");
            Integer areaId = Integer.parseInt((String) bodyParams.get("areaId"));
            Integer userId = Integer.parseInt((String) bodyParams.get("userId"));
            Integer estado = Integer.parseInt((String) bodyParams.get("correcta"));
            Question.createQuestion(preg, areaId, userId);
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //Edit a User (put es usado para crear o editar)
        put("/questions/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class
            );
            res.type("application/json");
            String id = req.params(":id");
            String pregunta = (String) bodyParams.get("preg");
            Integer areaId = Integer.parseInt((String) bodyParams.get("areaId"));
            Integer userId = Integer.parseInt((String) bodyParams.get("userId"));
            Integer correcta = Integer.parseInt((String) bodyParams.get("correcta"));
            Question toQuestion = Question.findById(id);
            if (toQuestion != null) {
                toQuestion.set("preg", pregunta);
                toQuestion.set("areaId", areaId);
                toQuestion.set("userId", userId);
                toQuestion.set("correcta", correcta);
                toQuestion.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });

        //-----------------------------------END QUESTION------------------//
        //
        //---------------------------ANSWER-----------------//
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
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class
            ); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            String resp = (String) bodyParams.get("resp");
            Integer tipoAnswer = Integer.parseInt((String) bodyParams.get("tipoAnswer"));
            Integer pregId = Integer.parseInt((String) bodyParams.get("pregId"));
            Answer.createAnswer(resp, tipoAnswer, pregId);
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //Edit a User (put es usado para crear o editar)
        put("/answers/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class
            );
            res.type("application/json");
            String id = req.params(":id");
            String respuesta = (String) bodyParams.get("resp");
            Integer tipoAnswer = Integer.parseInt((String) bodyParams.get("tipoAnswer"));
            Integer pregId = Integer.parseInt((String) bodyParams.get("pregId"));
            Answer toAnswer = Answer.findById(id);
            if (toAnswer != null) {
                toAnswer.set("resp", respuesta);
                toAnswer.set("tipoAnswer", tipoAnswer);
                toAnswer.set("pregId", pregId);
                toAnswer.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });














        //-------------------END ANSWER-------------------//

        //------------------------QUESTIONS_GAMES-------//
        /*
        //listar todas las preguntas de un juego.
        get("/questions_games/:idGame", (req, res) -> {
            res.type("application/json");
            String idGame = req.params(":idGame");
            List<QuestionGame> qGames = QuestionGame.where("gameId = ?", idGame);
            List<Map> qMapGames = new ArrayList<Map>();
            for (QuestionGame quest : qGames) {
                qMapGames.add(quest.getCompleteQuestionGame());
            }
            return new Gson().toJson(qMapGames);
        });


        //listar todos las preguntas de todos los games. --> para mi esta busqueda no es necesaria
        /*get("/questions_games", (req, res) -> {
            res.type("application/json");
            List<QuestionGame> qGames = QuestionGame.findAll();
            List<Map> qMapGames = new ArrayList<Map>();
            for(QuestionGame quest:qGames){
                qMapGames.add(quest.getCompleteQuestionGame());
            }
            return new Gson().toJson(qMapGames);
        });

        //agregar un nuevo QuestionGame. --> esto no deberia estar xq la tabla se completa por otras dos tablas
        post("/questions_games", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            Integer questId = Integer.parseInt((String) bodyParams.get("questionId"));
            Integer idGame = Integer.parseInt((String) bodyParams.get("gameId"));
            QuestionGame qGame = new QuestionGame(questId, idGame);
            qGame.saveIt();
            res.type("application/json");
            return new Gson().toJson(true);
        });*/
        //-----------------------------END QUESTIONS_GAMES------------------------//
        //----------------------ANSWER_GAMES--------------------------//
        /*
        //Listar todas las respuestas de un determinado Game --> para mi esta busqueda no es necesaria
        get("/answers_games/:gameId", (req, res) -> {
            res.type("application/json");
            String idGame = req.params(":gameId");
            List<AnswerGame> aGames = AnswerGame.where("gameId = ?", idGame);
            List<Map> aMapGames = new ArrayList<Map>();
            for (AnswerGame answ : aGames) {
                aMapGames.add(answ.getCompleteAnswerGame());
            }
            return new Gson().toJson(aMapGames);
        });

        //Listar todas las respuestas de todos los games.--> para mi esta busqueda no es necesaria
        /*get("/answers_games", (req, res) -> {
            res.type("application/json");
            List<AnswerGame> aGames = AnswerGame.findAll();
            List<Map> aMapGames = new ArrayList<Map>();
            for(AnswerGame answ:aGames){
                aMapGames.add(answ.getCompleteAnswerGame());
            }
            return new Gson().toJson(aMapGames);
        });
        //Agregar un nuevo AnswerGame en la base. --> esto no deberia estar xq la tabla se completa por otras dos tablas
        post("/answers_games", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            Integer answId = Integer.parseInt((String) bodyParams.get("answerId"));
            Integer idGame = Integer.parseInt((String) bodyParams.get("gameId"));
            AnswerGame aGame = new AnswerGame(answId, idGame);
            aGame.saveIt();
            res.type("application/json");
            return new Gson().toJson(true);
        });*/
        //------------------END ANSWER_GAMES----------------------//
        //--stat--
        //--end stat--
    }
}
