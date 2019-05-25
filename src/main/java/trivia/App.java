package trivia;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.util.*;
import java.text.SimpleDateFormat;
import org.javalite.activejdbc.Base;
import trivia.BasicAuth;

public class App {

    static User currentUser;


    public static void main(String[] args) {

        before((request, response) -> {
          Base.open();

          String headerToken = (String) request.headers("Authorization");

          if (
            headerToken == null ||
            headerToken.isEmpty() ||
            !BasicAuth.authorize(headerToken)
          ) {
            halt(401);
          }

          currentUser = BasicAuth.getUser(headerToken);
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
            int idUsr = User.userLogin((String) req.params(":dniUser"), (String) req.params(":pass"));
            if (idUsr > 0)
                return new Gson().toJson(true);
            return new Gson().toJson(false);
        });

        //Registration User
        post("/users", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            User.createUser((String) bodyParams.get("nom"), (String) bodyParams.get("ape"), (String) bodyParams.get("dni"), (String) bodyParams.get("pass"), Integer.parseInt((String) bodyParams.get("tipoUser")));
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
            User toUser = User.findById(req.params(":id"));
            if (toUser != null) {
                toUser.set("nom", (String) bodyParams.get("nom"));
                toUser.set("ape", (String) bodyParams.get("ape"));
                toUser.set("dni", (String) bodyParams.get("dni"));
                toUser.set("pass",(String) bodyParams.get("pass"));
                toUser.set("tipoUser", Integer.parseInt((String) bodyParams.get("tipoUser")));
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
        //---------------------END GAME----------//
        //
        //--------------------------QUESTIONS----------------------//
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

        //-----------------------------------FINQUESTION------------------//
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
        });

        //--------------------END ANSWER----------------------------//
    }
}
