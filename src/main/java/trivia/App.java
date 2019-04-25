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

public class App
{
    public static void main( String[] args ) {
      /*before((request, response) -> {
        Base.open("com.mysql.jdbc.driverr","jdbc:mysql://localhost:3306/trivia","root","root");
      });

      after((request, response) -> {
        Base.close();
    });*/

      Base.open("com.mysql.jdbc.Driverr","jdbc:mysql://localhost:3306/trivia","root","root");

      get("/hello/:name", (req, res) -> {
        return "hello" + req.params(":name");
      });

      /*post("/users", (req, res) -> {
        Map<String, Object> bodyParams = new Gson().fromJson(req.body(), Map.class);

        User user = new User();
        user.set("username", bodyParams.get("username"));
        user.set("password", bodyParams.get("password"));
        user.saveIt();

        res.type("application/json");

        return user.toJson(true);
    })*/
      Base.close();
    }
}
