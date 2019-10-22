package trivia.controllers;

import trivia.models.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

public class StatControllers {

    /*
    public static ModelAndView procesaStatArea(Request req, Response res) {

        Integer areaId = Integer.parseInt(req.queryParams("areaId"));
        Integer pregId = Integer.parseInt(req.queryParams("pregId"));
        List<Question> pregTotalHechasArea = Stat.questAskInArea(areaId);
        List<QuestionGame> pregCorrectHechasArea = Stat.questCorrectInArea(areaId);
        List<QuestionGame> pregIncorrectHechasArea = Stat.questIncorrectInArea(areaId);

        int pta = pregTotalHechasArea.size();
        int pca = pregCorrectHechasArea.size();
        int pia = pregIncorrectHechasArea.size();
        int nvpa = Stat.numTimeQuestInArea(pregTotalHechasArea, pregId);
        int nvpca = Stat.numTimeAnswCorretInAreaForQuest(pregCorrectHechasArea, pregId);
        int nvpia = Stat.numTimeAnswIncorretInAreaForQuest(pregIncorrectHechasArea, pregId);

        Map map = new HashMap();
        map.put("areaId", areaId);
        map.put("pregId", pregId);
        map.put("pregTotArea", pta); // Cantidad de preguntas hechas en el area
        map.put("pregCorrectArea", pca); // Cantidad de preguntas contestadas en forma correcta en el area
        map.put("pregIncorectArea", pia); // Cantidad de preguntas contestadas en forma incorrecta en el area
        map.put("nvpa", nvpa); // Cantidad de veces que se hizo una pregunta dada en un area dada
        map.put("nvpca", nvpca); // Cantidad de veces que una pregunta se contesto en forma correcta
        map.put("nvpia", nvpia); // cantidad de veces que una pregunta se contesto en forma incorrecta
        return new ModelAndView(map, "./views/respuesta_stat.mustache");
    }
*/

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

    /*
    public static ModelAndView procesaNvpca(Request req, Response res) {
        Integer areaId = Integer.parseInt(req.queryParams("areaId"));
        Integer pregId = Integer.parseInt(req.queryParams("pregId"));
        System.out.println("AREA ID DENTRO DE PROCESA NVPCA" + areaId);
        System.out.println("PREG ID DENTRO DE PRECESA NVPCA" + pregId);
        List<QuestionGame> pregCorrectHechasArea = Stat.questCorrectInArea(areaId);
        int nvpca = Stat.numTimeAnswCorretInAreaForQuest(pregCorrectHechasArea, pregId);
        Map r = new HashMap<>();
        r.put("nvpca", nvpca);
        return new ModelAndView(r, "./views/showQuestCorrectArea.html");
    }
    
    public static ModelAndView procesaNvpia(Request req, Response res) {
        Integer areaId = Integer.parseInt(req.queryParams("areaId"));
        Integer pregId = Integer.parseInt(req.queryParams("pregId"));
        System.out.println("AREA ID DENTRO DE PROCESA NVPIA" + areaId);
        System.out.println("PREG ID DENTRO DE PRECESA NVPIA" + pregId);
        List<QuestionGame> pregIncorrectHechasArea = Stat.questIncorrectInArea(areaId);
        int nvpia = Stat.numTimeAnswIncorretInAreaForQuest(pregIncorrectHechasArea, pregId);
        Map r = new HashMap<>();
        r.put("nvpia", nvpia);
        return new ModelAndView(r, "./views/showQuestIncorrectArea.html");
    }
*/

}

