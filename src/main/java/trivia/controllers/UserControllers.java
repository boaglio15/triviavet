package trivia.controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;
import trivia.models.*;

public class UserControllers {

    public  static ModelAndView procesaLoginWeb(Request req, Response res){
        Map map = new HashMap();
        String dni = req.queryParams("dni");
        String password = req.queryParams("pass");
        List<User> user = User.where("dni = ? and pass = ? ", dni, password);
        String mensaje_usuario;
        if (user.size()==1){
            mensaje_usuario= "El usuario con dni : "+dni+" se encuentra en la BD.";
        }else{
            mensaje_usuario= "El usuario con dni : "+dni+"  No se encuentra en la BD.";
        }
        map.put("msg" ,mensaje_usuario);
        return new ModelAndView(map, "./views/respuesta_login.mustache");
    }
}