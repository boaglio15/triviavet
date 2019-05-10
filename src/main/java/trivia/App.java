package trivia;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.util.*;
import java.text.SimpleDateFormat;

import org.javalite.activejdbc.Base;

import trivia.User;

public class App {

    public static void main(String[] args) {

        before((request, response) -> {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/trivia?nullNamePatternMatchesAll=true", "root", "root");
        });

        after((request, response) -> {
            Base.close();
        });

        // returns a User by id
        get("/users/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id"); // req.params -> indica que un parámetro de método debe estar vinculado a un PARAMETRO de solicitud web.
            User l = User.findById(id);
            Map m = l.getCompleteUser();
            return new Gson().toJson(m);
        });

        //returns all users
        get("/users", (req, res) -> {
            res.type("application/json");
            List<User> r = new ArrayList<User>();
            r = User.findAll();
            List<Map> rm = new ArrayList<Map>();
            for (User user:r) {rm.add(user.getCompleteUser());}
            return new Gson().toJson(rm);
        });

        //delete a User by id
        delete("/users/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            User l = User.findById(id);
            l.delete();
            return new Gson().toJson(true);
        });

        //Add User (post es usado para crear)
        post("/users", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            String nombre = (String) bodyParams.get("nom");
            String apellido = (String) bodyParams.get("ape");
            String dni = (String) bodyParams.get("dni");
            String password = (String) bodyParams.get("pass");
            Integer tipo = Integer.parseInt((String) bodyParams.get("tipoUser"));
            Integer nivel = Integer.parseInt((String) bodyParams.get("nivel"));
            User user = new User(nombre, apellido, dni, password, tipo, nivel);
            user.saveIt();
            res.type("application/json");
            return user.toJson(true);
        });

        //Edit a User (put es usado para crear o editar)
        put("/users/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            res.type("application/json");
            String id = req.params(":id");
            String nombre = (String) bodyParams.get("nom");
            String apellido = (String) bodyParams.get("ape");
            String dni = (String) bodyParams.get("dni");
            String password = (String) bodyParams.get("pass");
            Integer tipo = Integer.parseInt((String) bodyParams.get("tipoUser"));
            Integer nivel = Integer.parseInt((String) bodyParams.get("nivel"));
            User toUser = User.findById(id);
            System.out.println("toUser");
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

        //return todos los Games.
        get("/games", (req, res) -> {
            res.type("application/json");
            List<Game> r = new ArrayList<Game>();
            r = Game.findAll();
            List<Map> rm = new ArrayList<Map>();
            for (Game game:r) {rm.add(game.getCompleteGame());}
            return new Gson().toJson(rm);
        });

        //return un game de un usuario.
        get("/games/:userId", (req, res) -> {
            res.type("application/json");
            String user = req.params(":userId");
            List<Game> gameUser = Game.where("userId = ?", user);
            Map m = gameUser.get(0).getCompleteGame();
            return new Gson().toJson(m);
        });

        //agregar un nuevo game.
        post("/games", (req, res) -> {
            Map<String,Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            Integer usuarioId = Integer.parseInt((String) bodyParams.get("userId"));
            String f = (String) bodyParams.get("fecha");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            Date gameFecha = formatter.parse(f);
            Game game = new Game(usuarioId, gameFecha);
            game.saveIt();
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //borrar un game por id
        /*delete("/games/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            Game g = Game.findById(id);
            g.delete();
            return new Gson().toJson(true);
        });*/

        //listar todas las preguntas de un usuario.
        get("/questions_games/:idGame", (req, res) -> {
            res.type("application/json");
            String idGame = req.params(":idGame");
            List<QuestionGame> qGames = QuestionGame.where("gameId = ?", idGame);
            List<Map> qMapGames = new ArrayList<Map>();
            for(QuestionGame quest:qGames){
                qMapGames.add(quest.getCompleteQuestionGame());
            }
            return new Gson().toJson(qMapGames);
        });

        //listar todos las preguntas de todos los games.
        /*get("/questions_games", (req, res) -> {
            res.type("application/json");
            List<QuestionGame> qGames = QuestionGame.findAll();
            List<Map> qMapGames = new ArrayList<Map>();
            for(QuestionGame quest:qGames){
                qMapGames.add(quest.getCompleteQuestionGame());
            }
            return new Gson().toJson(qMapGames);
        });*/

        //agregar un nuevo QuestionGame.
        post("/questions_games", (req, res) -> {
            Map<String,Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            Integer questId = Integer.parseInt((String) bodyParams.get("questionId"));
            Integer idGame = Integer.parseInt((String) bodyParams.get("gameId"));
            QuestionGame qGame = new QuestionGame(questId, idGame);
            qGame.saveIt();
            res.type("application/json");
            return new Gson().toJson(true);
        });

        //Listar todas las respuestas de un determinado Game
        get("/answers_games/:gameId", (req, res) -> {
            res.type("application/json");
            String idGame = req.params(":gameId");
            List<AnswerGame> aGames = AnswerGame.where("gameId = ?", idGame);
            List<Map> aMapGames = new ArrayList<Map>();
            for(AnswerGame answ:aGames){
                aMapGames.add(answ.getCompleteAnswerGame());
            }
            return new Gson().toJson(aMapGames);
        });

        //Listar todas las respuestas de todos los games.
        /*get("/answers_games", (req, res) -> {
            res.type("application/json");
            List<AnswerGame> aGames = AnswerGame.findAll();
            List<Map> aMapGames = new ArrayList<Map>();
            for(AnswerGame answ:aGames){
                aMapGames.add(answ.getCompleteAnswerGame());
            }
            return new Gson().toJson(aMapGames);
        });*/

        //Agregar un nuevo AnswerGame en la base.
        post("/answers_games", (req, res) -> {
            Map<String,Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            Integer answId = Integer.parseInt((String) bodyParams.get("answerId"));
            Integer idGame = Integer.parseInt((String) bodyParams.get("gameId"));
            AnswerGame aGame = new AnswerGame(answId, idGame);
            aGame.saveIt();
            res.type("application/json");
            return new Gson().toJson(true);
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////77

        // returns an Answer by id
        get("/answers/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id"); // req.params -> indica que un parámetro de método debe estar vinculado a un PARAMETRO de solicitud web.
            Answer l = Answer.findById(id);
            Map m = l.getCompleteAnswer();
            return new Gson().toJson(m);
        });

        //returns all answers
        get("/answers", (req, res) -> {
            res.type("application/json");
            List<Answer> r = new ArrayList<Answer>();
            r = Answer.findAll();
            List<Map> rm = new ArrayList<Map>();
            for (Answer answer:r) {rm.add(answer.getCompleteAnswer());}
            return new Gson().toJson(rm);
        });

        //delete an Answer by id
        delete("/answers/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            Answer l = Answer.findById(id);
            l.delete();
            return new Gson().toJson(true);
        });

        //Add Answer (post es usado para crear)
        post("/answers", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            String respuesta = (String) bodyParams.get("resp");
            Integer tipoRespuesta = Integer.parseInt((String) bodyParams.get("tipoAnswer"));
            Integer idPregunta = Integer.parseInt((String) bodyParams.get("pregId"));
            Answer answer = new Answer(respuesta, tipoRespuesta, idPregunta);
            answer.saveIt();
            res.type("application/json");
            return answer.toJson(true);
        });

        //Edit an Answer (put es usado para crear o editar)
        put("/answers/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            res.type("application/json");
            String id = req.params(":id");
            String respuesta = (String) bodyParams.get("resp");
            Integer tipoRespuesta = Integer.parseInt((String) bodyParams.get("tipoAnswer"));
            Integer idPregunta = Integer.parseInt((String) bodyParams.get("pregId"));
            Answer toAnswer = Answer.findById(id);
            System.out.println("toAnswer");
            if (toAnswer != null) {
                toAnswer.set("resp", respuesta);
                toAnswer.set("tipoAnswer", tipoRespuesta);
                toAnswer.set("pregId", idPregunta);
                toAnswer.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });


        // returns an Area by id
        get("/areas/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id"); // req.params -> indica que un parámetro de método debe estar vinculado a un PARAMETRO de solicitud web.
            Area l = Area.findById(id);
            Map m = l.getCompleteArea();
            return new Gson().toJson(m);
        });

        //returns all areas
        get("/areas", (req, res) -> {
            res.type("application/json");
            List<Area> r = new ArrayList<Area>();
            r = Area.findAll();
            List<Map> rm = new ArrayList<Map>();
            for (Area area:r) {rm.add(area.getCompleteArea());}
            return new Gson().toJson(rm);
        });

        //delete an Area by id
        delete("/areas/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            Area l = Area.findById(id);
            l.delete();
            return new Gson().toJson(true);
        });

        //Add Area (post es usado para crear)
        post("/areas", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            String area = (String) bodyParams.get("nombreArea");
            Integer idUsuario = Integer.parseInt((String) bodyParams.get("userId"));
            Area area = new Area(area, idUsuario);
            area.saveIt();
            res.type("application/json");
            return area.toJson(true);
        });

        //Edit an Area (put es usado para crear o editar)
        put("/areas/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            res.type("application/json");
            String id = req.params(":id");
            String area = (String) bodyParams.get("nombreArea");
            Integer idUsuario = Integer.parseInt((String) bodyParams.get("userId"));
            Area toArea = Area.findById(id);
            System.out.println("toArea");
            if (toArea != null) {
                toArea.set("nombreArea", area);
                toArea.set("userId", idUsuario);
                toArea.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });


        // return a Question by id
        get("/questions/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id"); // req.params -> indica que un parámetro de método debe estar vinculado a un PARAMETRO de solicitud web.
            Question l = Question.findById(id);
            Map m = l.getCompleteQuestion();
            return new Gson().toJson(m);
        });

        //return all questions
        get("/questions", (req, res) -> {
            res.type("application/json");
            List<Question> r = new ArrayList<Question>();
            r = Question.findAll();
            List<Map> rm = new ArrayList<Map>();
            for (Question question:r) {rm.add(question.getCompleteQuestion());}
            return new Gson().toJson(rm);
        });

        //delete a Question by id
        delete("/questions/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            Question l = Question.findById(id);
            l.delete();
            return new Gson().toJson(true);
        });

        //Add Question (post es usado para crear)
        post("/questions", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class); //req.body -> indica que un parámetro del método debe estar vinculado al CUERPO de la solicitud web.
            String pregunta = (String) bodyParams.get("preg");
            Integer idArea = Integer.parseInt((String) bodyParams.get("idArea"));
            Integer idUsuario = Integer.parseInt((String) bodyParams.get("idUsuario"));
            Integer correct = Integer.parseInt((String) bodyParams.get("correcta"));
            Question question = new Question(pregunta, idArea, idUsuario, correct);
            question.saveIt();
            res.type("application/json");
            return question.toJson(true);
        });

        //Edit a Question (put es usado para crear o editar)
        put("/questions/:id", (req, res) -> {
            Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);
            res.type("application/json");
            String id = req.params(":id");
            String pregunta = (String) bodyParams.get("preg");
            Integer idArea = Integer.parseInt((String) bodyParams.get("areaId"));
            Integer idUsuario = Integer.parseInt((String) bodyParams.get("userId"));
            Integer correct = Integer.parseInt((String) bodyParams.get("correcta"));
            Question toQuestion = Question.findById(id);
            System.out.println("toQuestion");
            if (toQuestion != null) {
                toQuestion.set("preg", pregunta);
                toQuestion.set("areaId", idArea);
                toQuestion.set("userId", idUsuario);
                toQuestion.set("correcta", correct);
                toQuestion.saveIt();
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });
}
