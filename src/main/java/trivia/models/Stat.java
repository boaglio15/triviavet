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
   
   
    // ----------------STAT ADMIN---------------------//
    // retorna las preguntas hechas en un area (correctas o incorrectas)
    // considerando todas las preguntas hechas a todos los jugadores
    public static List<Question> questAskInArea(int areaId) {
        
        return Question.findBySQL( "SELECT questions.id FROM questions INNER JOIN questions_games ON questions.id = questions_games.questionId WHERE questions.areaId = ?", areaId);
    
    }

    // retorna las preguntas hechas en un area (correctas e incorrectas)
    // considerando todas las preguntas (pero sin repetidas) hechas a todos los jugadores
    /*
    public static List<Question> questDistincAskInArea(int areaId) {
        
        return Question.findBySQL("SELECT DISTINCT questions.id, questions.preg FROM questions INNER JOIN questions_games ON questions.id = questions_games.questionId WHERE questions.areaId = ?", areaId);

    }*/

    //genera una lista de map con el id y la pregunta, de las preguntas (correctas, incorrectas y sin repetidas) hechas en un area
    //se usa para listar por pantalla
    public static Map showQuestAskInArea(int areaId) {
        List<Question> pregTotalHechasArea = Stat.questAskInArea(areaId);
        int pta = pregTotalHechasArea.size();
        Map r = new HashMap();
        if (pta == 0) {
            r.put("error", "no hay preguntas hechas en el area");
        } else {
            r.put("pregTotArea", pta);
            List<QuestionGame> pregCorrectHechasArea = Stat.questCorrectInArea(areaId);
            //List<QuestionGame> pregIncorrectHechasArea = Stat.questIncorrectInArea(areaId);
            int pca = pregCorrectHechasArea.size();
            int pia = pta - pca;//pregIncorrectHechasArea.size();
            r.put("pregCorrectArea", pca);
            r.put("pregIncorrectArea", pia);
            r.put("areaId", areaId);
        }
       return r;
        /*
        List<Map> listMap = new ArrayList<Map>();
        List<Question> qa = questDistincAskInArea(areaId);
        if (qa == null) {
            System.out.println("LISTA NULL ");
            Map r = new HashMap();
            r.put("msg", "no hay preguntas hechas en el area");
            return r;
        } else {
            System.out.println("LISTA  ");
            for (Question q : qa) {
                Map a = new HashMap();
                String id = Integer.toString(q.getInteger("id"));
                System.out.println("ID " + id);
                System.out.println("PREG " +  q.getString("preg"));
                a.put("id", q.getInteger("id"));
                a.put("preg", q.getString("preg"));
                listMap.add(a);
            }
            Map r = new HashMap();
            r.put("listMapQuestArea", listMap);
            r.put("areaId", areaId);
            List<Question> pregTotalHechasArea = Stat.questAskInArea(areaId);
            int pta = pregTotalHechasArea.size();
            r.put("pregTotArea", pta);
            System.out.println("MAPEO DE AREA " + r.get("areaId"));
            return r;
        }*/
    }

    // retorna el id de las preguntas que fueron contestadas en forma correcta en todos lo juegos en un area (hay repetidas)
    public static List<QuestionGame> questCorrectInArea(int areaId) {
        
        return QuestionGame.findBySQL("SELECT questions_games.questionId FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = ? and questions_games.estado = 1", areaId);

    }

    // retorna el id de las preguntas que fueron contestadas en forma correcta en todos lo juegos en un area (sin repetidas)
    // se usa para listar por pantalla
    public static List<QuestionGame> questCorrectDistinctInArea(int areaId) {

        return QuestionGame.findBySQL("SELECT DISTINCT questions_games.questionId FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = ? and questions_games.estado = 1", areaId);
    }

    //genera una lista de map con el id y la pregunta, de las preguntas hechas en forma correcta en un area
    //se usa para listar por pantalla
    public static Map showQuestCorrectArea(int areaId) {
        List<Map> listMap = new ArrayList<Map>();
        List<QuestionGame> qca = questCorrectDistinctInArea(areaId);
       
            for (QuestionGame q : qca) {
                Map a = new HashMap();
                Integer id = q.getInteger("questionId");
                a.put("id", id);
                List<Question> qq = Question.where("id =  ?", id);
                String preg = qq.get(0).getString("preg");
                a.put("preg", preg);
                listMap.add(a);
            }
            Map r = new HashMap();
            r.put("listMapQuestCorrectArea", listMap);
            r.put("areaId", areaId);
            return r;
        
    }

     // retorna el id de las preguntas que fueron contestadas en forma incorrecta  en todos lo juegos en un area (hay repetidas)
    public static List<QuestionGame> questIncorrectInArea(int areaId) {
       
        return QuestionGame.findBySQL("SELECT questions_games.questionId FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = ? and questions_games.estado = 0", areaId);
  
    }

    
    public static List<QuestionGame> questIncorrectDisntinctArea(int areaId) {

        return QuestionGame.findBySQL("SELECT DISTINCT questions_games.questionId FROM questions_games INNER JOIN questions ON questions.id = questions_games.questionId WHERE questions.areaId = ? and questions_games.estado = 0", areaId);

    }

    public static Map showQuestIncorrectArea(int areaId) {
        List<Map> listMap = new ArrayList<Map>();
        List<QuestionGame> qia = questIncorrectDisntinctArea(areaId);
       
            for (QuestionGame q : qia) {
                Map a = new HashMap();
                Integer id = q.getInteger("questionId");
                a.put("id", id);
                List<Question> qq = Question.where("id =  ?", id);
                String preg = qq.get(0).getString("preg");
                a.put("preg", preg);
                listMap.add(a);
            }
            Map r = new HashMap();
            r.put("listMapQuestIncorrectArea", listMap);
            return r;
        
    }
    

    // retorna la cantidad de veces que una pregunta se hizo en una area
    public static int numTimeQuestInArea(List<Question> pregTotalHechasArea, Integer pregId) {
        int cant = 0;
        for (Question q : pregTotalHechasArea) {
            if (pregId.equals((Integer) q.getId())) {
                cant++;
            }
        }
        ;
        return cant;
    }

    

    

    // retorna la cantidad de veces que una pregunta se contesto en forma correcta
    // en un area
    public static int numTimeAnswCorretInAreaForQuest(List<QuestionGame> pregCorrectArea, Integer pregId) {
        int cant = 0;
        for (QuestionGame q : pregCorrectArea) {
            if (pregId.equals(q.getQuestionId())) {
                cant++;
            }
        }
        return cant;
    }

    // retorna la cantidad de veces que una pregunta se contesto en forma incorreta
    // en un area
    public static int numTimeAnswIncorretInAreaForQuest(List<QuestionGame> pregIncorrectArea, Integer pregId) {
        int cant = 0;
        for (QuestionGame q : pregIncorrectArea) {
            if (pregId.equals(q.getQuestionId())) {
                cant++;
            }
        }
        return cant;
    }

    // -------------------END STAT ADMIN----------------------//
}
