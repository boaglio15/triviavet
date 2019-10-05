package trivia.models;

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

    public Map getCompleteUser() {
        Map m = new HashMap();
        m.put("userId", this.getId());
        m.put("cantCorrectas", this.getCantCorrectas());
        m.put("cantIncorrectas", this.getCantIncorrectas());
        return m;
    }

    //------------------STAT USERS-----------------//
    // actualiza en la BD la estadistica para un jugador
    public static void createStat(int gameId, int cantCorrectas, int cantIncorrectas) {
        Stat stat = new Stat(gameId, cantCorrectas, cantIncorrectas);
        stat.saveIt();
    }

    
    //retorna las estadisticas por area jugada o no.
    public static Map getStatPlayArea(String userId, String areaId) {
    	List<UserArea> areas = UserArea.getAreasUser(userId, areaId);   //det completada, nivel para un user en un area dada
        Map m = new HashMap();  
        if (areas.isEmpty()) {
            m.put("isPlayArea", 0); //caso area no jugada
        } else {
            List<Integer> pregHechas = Game.getAllQuestionGameArea(userId, areaId);    //retorna el id de todas las preguntas en un area
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
    
    //------------------END STAT USERS-------------------//
   
   
    //----------------STAT ADMIN---------------------//
    // retorna las preguntas hechas en un area (correctas o incorrectas)
    // considerando todas las preguntas hechas a todos los jugadores
    public static List<Question> questAskInArea(int areaId) {
       if (areaId == 1) {
        return Question.findBySQL("SELECT questions.id FROM questions INNER JOIN questions_games ON questions.id = questions_games.questionId WHERE questions.areaId = 1"); 
       }

       if (areaId == 2) {
        return Question.findBySQL("SELECT questions.id FROM questions INNER JOIN questions_games ON questions.id = questions_games.questionId WHERE questions.areaId = 2"); 
       }

       if (areaId == 3) {
        return Question.findBySQL("SELECT questions.id FROM questions INNER JOIN questions_games ON questions.id = questions_games.questionId WHERE questions.areaId = 3"); 
       } else {
           return null;
       }       
    }

    // retorna la cantidad de veces que una pregunta se hizo en una area
    public static int numTimeQuestInArea(List<Question> pregTotalHechasArea, Integer pregId) {
        int cant = 0;  
        for (Question q : pregTotalHechasArea) {
            if (pregId.equals((Integer) q.getId())) {
                cant++;
            }
        };
        return cant;
    }

    // retorna las preguntas que fueron contestadas en forma correcta en un area
    public static List<QuestionGame> questCorrectInArea(int areaId) {
        if (areaId == 1) {
            return QuestionGame.findBySQL(
            "SELECT questions_games.id FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = 1 and questions_games.estado = 1");
        }

        if (areaId == 2) {
            return QuestionGame.findBySQL(
            "SELECT questions_games.id FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = 2 and questions_games.estado = 1");
        }

        if (areaId == 3) {
            return QuestionGame.findBySQL(
            "SELECT questions_games.id FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = 3 and questions_games.estado = 1");
        } else {
            return null;
        } 
    }   

    // retorna las preguntas que fueron contestadas en forma incorrecta en un area
    public static List<QuestionGame> questIncorrectInArea(int areaId) {
        if (areaId == 1) {
            return QuestionGame.findBySQL(
            "SELECT questions_games.id FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = 1 and questions_games.estado = 0");
        }

        if (areaId == 2) {
            return QuestionGame.findBySQL(
            "SELECT questions_games.id FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = 2 and questions_games.estado = 0");
        }

        if (areaId == 3) {
            return QuestionGame.findBySQL(
            "SELECT questions_games.id FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = 3 and questions_games.estado = 0");
        } else {
            return null;
        } 
    }

    // retorna la cantidad de veces que una pregunta se contesto en forma correcta en un area
    public static int numTimeAnswCorretInAreaForQuest(List<QuestionGame> pregCorrectArea, Integer pregId) {
        int cant = 0;
        for (QuestionGame q : pregCorrectArea) {
            if (pregId.equals(q.getQuestionId())) {
                cant++;
            }
        }
        return cant;
    }


    // retorna la cantidad de veces que una pregunta se contesto en forma incorreta en un area
    public static int numTimeAnswIncorretInAreaForQuest(List<QuestionGame> pregIncorrectArea, Integer pregId) {
        int cant = 0;
        for (QuestionGame q : pregIncorrectArea) {
            if (pregId.equals(q.getQuestionId())) {
                cant++;
            }
        }
        return cant;
    }

    //-------------------END  STAT ADMIN----------------------//
}
