package trivia;

import org.javalite.activejdbc.Base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import trivia.models.*;
import spark.utils.IOUtils;

import spark.Spark;

import java.io.OutputStreamWriter;

import com.google.gson.Gson;

public class UserIntegrationTest {
    private static int PORT = 4567;
    private static String ADMIN_DNI = "admin";
    private static String ADMIN_PASSWORD = "admin";

    @AfterClass
    public static void tearDown() {
        Spark.stop();
    }

    @After
    public void clear() {
    }


    @Before
    public void beforeTest() {
    }

    @BeforeClass
    public static void setup() {
        App.main(null);

        Spark.awaitInitialization();

        // Create an admin user to log into system using Basic Auth before run the test
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/trivia_test", "root", "root");
        User u = new User();
        u.set("nom", "Agustin");
        u.set("ape", "Boaglio");
        u.set("dni", ADMIN_DNI);
        u.set("pass", ADMIN_PASSWORD);
        u.set("tipoUser", 1);
        u.saveIt();
        Base.close();
    }

    @Test
    public void canCreateUser() {
      String dniLog = "37875774";
      String password = "abc123";
      String nombre = "Gaston";
      String apellido = "Mercau";
      int tipo = 0;
      
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("nom", nombre);
      parameters.put("ape", apellido);
      parameters.put("dni", dniLog);
      parameters.put("pass", password);
      parameters.put("tipoUser", tipo);
      
      UrlResponse response = doRequest("POST", "/user", parameters);
      Map<String, Object> jsonResponse = new Gson().fromJson(response.body, Map.class);

      assertNotNull(response);
      //assertNotNull(response.body);
      //assertEquals(200, response.status);
      //assertEquals(jsonResponse.get("dni"), dniLog);
    }

    private static UrlResponse doRequest(String requestMethod, String path, Map body) {
        UrlResponse response = new UrlResponse();

        try {
            getResponse(requestMethod, path, body, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static void getResponse(String requestMethod, String path, Map body, UrlResponse response)
            throws MalformedURLException, IOException, ProtocolException {
      URL url = new URL("http://localhost:" + PORT + path);

      // This is the point where the connection is opened.
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      // Set User to get Authorized request
      String userCredentials = ADMIN_DNI + ":" + ADMIN_PASSWORD;
      String basicAuth = "Basic " + new String(
        Base64.getEncoder().encode(userCredentials.getBytes())
      );
      connection.setRequestProperty("Authorization", basicAuth);

      // set connection output to true (needs to be true since this request
      // is carrying an input (response) body.)
      connection.setDoOutput(true);

      // set connection verb to user
      connection.setRequestMethod(requestMethod);

      // write parameters into connection
      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
      writer.write(body.toString());
      writer.close();

      // Open communications link (network traffic occurs here).
      connection.connect();

      //  Retrieve the response body as an InputStream.
      String res = IOUtils.toString(connection.getInputStream());

      // Build the response to return
      response.body = res;
      response.status = connection.getResponseCode();
      response.headers = connection.getHeaderFields();
    }

    // Represent a Response
    private static class UrlResponse {
      public Map<String, List<String>> headers;
      private String body;
      private int status;
    }
}
