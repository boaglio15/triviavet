package trivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.javalite.activejdbc.Model;

public class Question extends Model {

    private String preg;
    private int areaId;
    private int userId;

    public Question() {
    }

    public Question(String preg, int areaId, int userId) {
        set("preg", preg);
        set("areaId", areaId);
        set("userId", userId);
    }

    public String getPreg() {
        return this.getString("preg");
    }

    public Integer getAreaId() {
        return this.getInteger("areaId");
    }

    public Integer getUserId() {
        return this.getInteger("userId");
    }

    public Map getCompleteQuestion() {
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("preg", this.getPreg());
        m.put("areaId", this.getAreaId());
        m.put("userId", this.getUserId());
        return m;
    }

    public static List<Map> getAllQuestion() {
        List<Question> rq = new ArrayList<Question>();
        rq = Question.findAll();
        List<Map> qm = new ArrayList<Map>();
        for (Question ques : rq) {
            qm.add(ques.getCompleteQuestion());
        }
        return qm;
    }

    public static Question getQuestion(String id) {
        Question q = Question.findById(id);
        return q;
    }

    public static void deleteQuestion(String id) {
        Question q = Question.findById(id);
        q.delete();
    }

    public static void createQuestion(String preg, int areaId, int userId) {
        Question ques = new Question(preg, areaId, userId);
        ques.saveIt();
    }

    //retorna el id de todas las preguntas hechas en un juego para un area determinada
    public static List<Integer> getAllQuestionGameArea(String gameId, String areaId) {
        List<Integer> questId = new ArrayList<Integer>();
        List<Integer> qId = QuestionGame.allQuestionGame(gameId);
        for (Integer quest : qId) {
            Question qq = getQuestion(Integer.toString(quest));
            if ((Integer) qq.getAreaId() == Integer.parseInt((String) areaId)) {
                questId.add((int) qq.getId());
            }
        }
        return questId;
    }

    //retorna el id de todas las preguntas en un area
    public static List<Integer> allQuestionArea(String areaId) {
        List<Question> quest = Question.where("areaId = ?", areaId);
        List<Integer> listQuestions = new ArrayList<Integer>();
        for (Question questA : quest) {
            listQuestions.add((Integer) questA.getId());
        }
        return listQuestions;
    }

    //REVISAR ESTE METODO LA FORMA EN QUE SELECCIONA ¡¡¡
    //retorna una pregunta para hacer
    public static String selectQuestion(List<Integer> pregHechas, List<Integer> pregEnArea) {
        
        for (Integer questId : pregHechas) {
            String id = "4"; //considerando las preguntas que hay en un area selccionar unu id  
            Question pregSelec = getQuestion(id);
            if (questId != pregSelec.getId()) {
                return pregSelec.getPreg();
            }
        }
        return null;
    }
}