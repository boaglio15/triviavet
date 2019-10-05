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



}