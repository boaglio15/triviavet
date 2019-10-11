package trivia.controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;
import trivia.models.*;

public class QuestionController {

    public static ModelAndView procesaQuestion(Request req, Response res){
        Map map = new HashMap();
        String pregunta = req.queryParams("preg");
        int areaId = Integer.parseInt(req.queryParams("area"));
        String respuesta1 = req.queryParams("resp1");
        String respuesta2 = req.queryParams("resp2");
        String respuesta3 = req.queryParams("resp3");
        String respuesta4 = req.queryParams("resp4");
        Question.newQuestionAdmin(pregunta, respuesta1, respuesta2, respuesta3, respuesta4, areaId);

        String mensaje_usuario = "Pregunta guardada";
        map.put("msg" ,mensaje_usuario);
        return new ModelAndView(map, "./views/logged.html");
    }
}