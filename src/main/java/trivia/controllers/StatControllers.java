package trivia.controllers;

import trivia.models.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

public class StatControllers {

    public  static ModelAndView procesaStatArea(Request req, Response res){
        
        //String areaId = req.queryParams("areaId");
        //String pregId = req.queryParams("pregId");
        Integer areaId = Integer.parseInt(req.queryParams("areaId"));
        Integer pregId = Integer.parseInt(req.queryParams("pregId"));
        List<Question> pregTotalHechasArea = Stat.questAskInArea(areaId);
        List<QuestionGame> pregCorrectHechasArea = Stat.questCorrectInArea(areaId);
        List<QuestionGame> pregIncorrectHechasArea = Stat.questIncorrectInArea(pregId);

        int pta = pregTotalHechasArea.size();
        int pca = pregCorrectHechasArea.size();
        int pia = pregIncorrectHechasArea.size();
        int nvpa = Stat.numTimeQuestInArea( pregTotalHechasArea, pregId);
        int nvpca = Stat.numTimeAnswCorretInAreaForQuest(pregCorrectHechasArea, pregId); 
        int nvpia = Stat.numTimeAnswIncorretInAreaForQuest(pregIncorrectHechasArea, pregId); 
        
        Map map = new HashMap();
        map.put("areaId", areaId);
        map.put("pregId", pregId);
        map.put("pregTotArea", pta);        //Cantidad de preguntas hechas en el area
        map.put("pregCorrectArea", pca);    //Cantidad de preguntas contestadas en forma correcta en el area
        map.put("pregIncorectArea",pia);    //Cantidad de preguntas contestadas en forma incorrecta en el area
        map.put("nvpa" ,nvpa);              //Cantidad de veces que se hizo una pregunta dada en un area dada
        map.put("nvpca", nvpca);            //Cantidad de veces que una pregunta se contesto en forma correcta
        map.put("nvpia", nvpia);            //cantidad de veces que una pregunta se contesto en forma incorrecta
        return new ModelAndView(map, "./views/respuesta_stat.mustache");
    }   
}
