package trivia.controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;
import trivia.models.*;

public class UserControllers {

    
    public static ModelAndView login(Request req, Response res, String sessionName) {

        String dni = req.session().attribute(sessionName);  // Get session attribute 'SESSION_NAME'
        System.out.println("SESSION NAME EN LOGIN " + dni);
        Map map = new HashMap();
        if (dni == null) {
            return new ModelAndView(map, "./views/flogin.mustache");
        } else {
            return new ModelAndView(map, "./views/logged.html");
        }

    }

    public static ModelAndView logout(Request req, Response res, String sessionName) {

        System.out.println("SESSION A TERMINAR " + sessionName);
        Map map = new HashMap();
        if (req.session().attribute("sessionName") != null) {
             req.session().removeAttribute("sessionName");
             System.out.println("SESION TERMINADA");
        }

        res.redirect("/");
        return null;
    }

    public static ModelAndView procesaLoginWeb(Request req, Response res, String sessionName) {

        Map map = new HashMap();
        String dni = req.queryParams("dni");
        String password = req.queryParams("pass");

        System.out.println("DNI" + dni);

        //busca en la base de datos de usuarios
        if (User.validUser(dni, password)) { //caso usuario valido

            System.out.println("USER ACEPTADO");
            User currentUser = User.findUserByDni(dni);  //obtiene el usuario logueado
            System.out.println("RESUL DE ISADMIN " + currentUser.isAdmin());

            if (currentUser.isAdmin()) { //caso user admin

                if (dni != null) { // el dni es el nombre de la sesion
                    req.session(true);   // create and return session
                    req.session().attribute("sessionName", dni); // Set session attribute 'dni'

                    System.out.println("USUARIO ADMIN + SESION CREADA CON NOMBRE " + req.session().id());  // Get session id
                    return new ModelAndView(map, "./views/logged.html");
                } else {
                    System.out.println("SESION YA CREADA " + req.session().id());
                }
                return null;

            } else { // caso user no admin
                System.out.println("USUARIO NO ADMIN");
                res.redirect("/");
            }

        } else { // caso usuario invalido

            System.out.println("USER INVALID");
            res.redirect("/");
        }

        return null;
    }

}
