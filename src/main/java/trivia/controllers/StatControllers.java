package trivia.controllers;

import trivia.models.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

public class StatControllers {

    
    public static ModelAndView procesaShowQuestInArea(Request req, Response res) {
        
        Integer areaId = Integer.parseInt(req.queryParams("areaId"));
        Map listMapQuesArea = Stat.showQuestAskInArea(areaId);
        return new ModelAndView(listMapQuesArea, "./views/showQuestArea.html");
    }

    public static ModelAndView procesaShowQuestCorrectArea(Request req, Response res) {
        
        System.out.println("ID AREA DENTRO DE QUEST COREC AREA " + req.queryParams("areaId"));
        Integer areaId = Integer.parseInt(req.queryParams("areaId"));
        Map listMapQuestCorrectArea = Stat.showQuestCorrectArea(areaId);
        return new ModelAndView(listMapQuestCorrectArea, "./views/showQuestCorrectArea.html");
    }

    public static ModelAndView procesaShowQuestIncorrectArea(Request req, Response res) {
        
        Integer areaId = Integer.parseInt(req.queryParams("areaId"));
        Map listMapQuestIncorrectArea = Stat.showQuestIncorrectArea(areaId);
        return new ModelAndView(listMapQuestIncorrectArea, "./views/showQuestIncorrectArea.html");

    }

}

