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
    }
}
