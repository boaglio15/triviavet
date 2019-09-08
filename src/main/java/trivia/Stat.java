package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import java.util.*;

@Table("stats")
public class Stat extends Model {

    private int gameId;
    private int cantCorrectas;
    private int cantIncorrectas;

    public Stat() {
    }

    public Stat(int gameId, int cantCorrectas, int cantIncorrectas) {
        set("userId", gameId);
        set("cantCorrectas");
        set("cantIncorrectas");
    }

    public int getUserId() {
        return this.getInteger("userId");
    }

    public int getCantCorrectas() {
        return this.getInteger("cantCorrectas");
    }

    public int getCantIncorrectas() {
        return this.getInteger("CantIncorrectas");
    }

    // actualiza en la BD la estadistica para un jugador
    public static void createStat(int gameId, int cantCorrectas, int cantIncorrectas) {
        Stat stat = new Stat(gameId, cantCorrectas, cantIncorrectas);
        stat.saveIt();
    }

    
    //retorna las estadisticas por area jugada o no.
    public static Map getStatPlayArea(String areaId) {
        List<UserArea> areas = UserArea.getAreasUser(areaId);   //det completada, nivel para un user en un area dada
       Map m = new HashMap();  
        if (areas.isEmpty()) {
            m.put("isPlayArea", 0); //caso area no jugada
        } else {
            List<Integer> pregHechas = Game.allQuestionArea(areaId);    //retorna el id de todas las preguntas en un area
            String userId = String.valueOf(areas.get(0).getUserId());   //det el userId del jugador en el area dada
            System.out.println("USER ID OBTENIDO EN STAT " + userId);
            int correct = Game.getCantQuestCorrecArea(userId, pregHechas);
            int incorrect = pregHechas.size() - correct;
            m.put("isPlayArea", 1);
            m.put("completada", areas.get(0).getCompletada());
            m.put("nivel", areas.get(0).getNivel());
            m.put("cantCorrect", correct);
            m.put("cantIncorrect", incorrect);
        }
        return m;
    }
    
   
   public Map getCompleteUser() {
        Map m = new HashMap();
        m.put("userId", this.getId());
        m.put("cantCorrectas", this.getCantCorrectas());
        m.put("cantIncorrectas", this.getCantIncorrectas());
        return m;
    }

}
