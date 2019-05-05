package trivia;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            String j = l.getNom(); //hay que hacer que retorne todo el registro
            return new Gson().toJson(j);

        });

        //returns all users
        get("/users", (req, res) -> {
            res.type("application/json");
            List<User> r = new ArrayList<User>();
            r = User.findAll();
            String j = r.get(0).getNom(); //hay que hacer que retorne todos los datos de los registros
            return new Gson().toJson(j);
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
            int tipo = Integer.parseInt((String) bodyParams.get("tipoUser"));
            int nivel = Integer.parseInt((String) bodyParams.get("nivel"));
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
            int tipo = Integer.parseInt((String) bodyParams.get("tipoUser"));
            int nivel = Integer.parseInt((String) bodyParams.get("nivel"));
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
                //System.out.println(toUser);
                return new Gson().toJson(true);
            } else {
                return new Gson().toJson(false);
            }
        });
    }
}
