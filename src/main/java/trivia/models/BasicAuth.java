package trivia.models;

import java.io.*;
import java.util.Base64;

//import trivia.User;

public class BasicAuth {

    public static Boolean authorize(String headerAuth) { //headerAuth se le pasa el token recibido en trivia de la app
        final String[] creds = getCredentials(headerAuth);

        return User.findFirst("dni = ? AND pass = ?", creds[0], creds[1]) != null; //verifica que el user y pass esten en la bd
    }

    public static User getUser(String headerAuth) {
        final String[] creds = getCredentials(headerAuth);

        return User.findFirst("dni = ?", creds[0]); //retorna el user pasado desde la app
    }

    private static String[] getCredentials(String headerAuth) {
        try {
            String base64Credentials = headerAuth.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, "UTF-8");

            return credentials.split(":", 2); //retorna el user y pass mandado desde la app

        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is unknown");
        }
    }
}
