package trivia;

import static spark.Spark.get;
import static spark.Spark.post;

import static spark.Spark.before;
import static spark.Spark.after;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

import trivia.User;

import com.google.gson.Gson;
import java.util.Map;
import java.util.*;

public class App
{
    public static void main( String[] args )
    {
      //before((request, response) -> {
        //Base.open();
      //});
      Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");



      get("/hello/:name", (req, res) -> {
          //System.out.println("hola mundo");
        return "hello" + req.params(":name");
    });

      /*post("/users", (req,res) -> {
          User newUser = new User();
          newUser.set("nom", "Agustin");
          newUser.set("ape", "Boaglio");
          newUser.set("dni", "37875774");
          newUser.set("tipo", "ADMIN");
          newUser.saveIt();

          res.type("application/json");

          return newUser.toJson(true);
      });*/

      get("/users", (req, res) -> {
            res.type("application/json");
            List<User> r = User.findAll();
            return new Gson().toJson(r);
      });

      //after((request, response) -> {
        Base.close();
      //});
      /*post("/users", (req, res) -> {
        Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);

        User user = new User();
        user.set("username", bodyParams.get("username"));
        user.set("password", bodyParams.get("password"));
        user.saveIt();

        res.type("application/json");

        return user.toJson(true);
    });*/
    }
}
