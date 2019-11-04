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

    public static ModelAndView listQuestion(Request req, Response res){
        Map lista = new HashMap();
        Integer areaId = Integer.parseInt(req.queryParams("area"));
        return new ModelAndView(Area.getQuestionArea(areaId), "./views/modifyQuestion.html");
    }

    public static ModelAndView selectQuestion(Request req, Response res){
        Integer idQuestion = Integer.parseInt(req.queryParams("idQuest"));
        Map<String, Object> questAnswer = Question.questComplete(idQuestion);
        questAnswer.put("idQuest", idQuestion);
        return new ModelAndView(questAnswer, "./views/putQuest.html");
    }

    public static ModelAndView modPregunta(Request req, Response res){
        Integer idQuest = Integer.parseInt(req.queryParams("idQuest"));
        String pregunta = req.queryParams("preg");
        String respuesta1 = req.queryParams("resp1");
        String respuesta2 = req.queryParams("resp2");
        String respuesta3 = req.queryParams("resp3");
        String respuesta4 = req.queryParams("resp4");
        Question.modificarQuestion(idQuest, pregunta, respuesta1, respuesta2, respuesta3, respuesta4);
        Map<String, Object> map = new HashMap<String, Object>();
        return new ModelAndView(map, "./views/logged.html");
    }
}