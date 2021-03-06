package trivia.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.javalite.activejdbc.Model;

public class Question extends Model {
    static {
        validatePresenceOf("preg").message("Ingrese la pregunta");
    }

    private String preg;
    private int areaId;
    private int userAdminId;

    public Question() {
    }

    public Question(String preg, int areaId, int userAdminId) {
        set("preg", preg);
        set("areaId", areaId);
        set("userAdminId", userAdminId);
    }
    

    public String getPreg() {
        return this.getString("preg");
    }

    public Integer getAreaId() {
        return this.getInteger("areaId");
    }

    public Integer getUserId() {
        return this.getInteger("userAdminId");
    }

    public Map getCompleteQuestion() {
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("preg", this.getPreg());
        m.put("areaId", this.getAreaId());
        m.put("userAdminId", this.getUserId());
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

    public static void createQuestion(String preg, int areaId, int userAdminId) {
        Question ques = new Question(preg, areaId, userAdminId);
        ques.saveIt();
    }

    public static void newQuestionAdmin(String preg, String resp1, String resp2,
    String resp3, String resp4, int areaId){
        Question newQuestion = new Question();
        newQuestion.set("preg", preg);
        newQuestion.set("areaId", areaId);
        //newQuestion.set("userAdminId", adminId);
        newQuestion.saveIt();
        int idQuestion = newQuestion.getInteger("id");
        Answer.createAnswer(resp1, 1, idQuestion);
        Answer.createAnswer(resp2, 0, idQuestion);
        Answer.createAnswer(resp3, 0, idQuestion);
        Answer.createAnswer(resp4, 0, idQuestion);
    }

    public static Map<String, Object> questComplete(Integer idQuestion){
        Question quest = Question.findById(idQuestion);
        Answer correct = Answer.findFirst("pregId = ? and tipoAnswer = ?", idQuestion, 1);
        List<Answer> incorrects = Answer.findBySQL("SELECT * FROM answers WHERE pregId = ? and tipoAnswer = ?", idQuestion, 0);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("question", quest.get("preg"));
        res.put("correcta", correct.get("resp"));
        res.put("incorrecta1", incorrects.get(0).get("resp"));
        res.put("incorrecta2", incorrects.get(1).get("resp"));
        res.put("incorrecta3", incorrects.get(2).get("resp"));
        return res;
    }

    public static void modificarQuestion(Integer idQuestion, String preg, String resp1, String resp2,
    String resp3, String resp4){
        Question quest = Question.findById(idQuestion);
        Answer correct = Answer.findFirst("pregId = ? and tipoAnswer = ?", idQuestion, 1);
        List<Answer> incorrect = Answer.where("pregId = ? and tipoAnswer = ?", idQuestion, 0);
        quest.set("preg", preg);
        correct.set("resp", resp1);
        incorrect.get(0).set("resp", resp2).saveIt();
        incorrect.get(1).set("resp", resp3).saveIt();
        incorrect.get(2).set("resp", resp4).saveIt();
        quest.saveIt();
        correct.saveIt();
    }




}
