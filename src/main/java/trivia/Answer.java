package trivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.javalite.activejdbc.Model;

public class Answer extends Model {
    static {
        validatePresenceOf("resp").message("Ingrese la respuesta");
    }

    private String resp;
    private int tipoAnswer;
    private int pregId;
    
    public Answer(){}
    
    public Answer(String resp, int tipoAnswer, int pregId){
        set("resp", resp);
        set("tipoAnswer", tipoAnswer);
        set("pregId", pregId);
    }
    
    public String getResp() {
        return this.getString("resp");
    }
    
    public String getTipoAnswer() {
        return this.getString("tipoAnswer");
    }
    
    public String getPregId() {
        return this.getString("pregId");
    }
    
    public Map getCompleteAnswer() {
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("resp", this.getResp());
        m.put("tipoAnswer",this.getTipoAnswer());
        m.put("pregId", this.getPregId());
        return m;
    }
    
    public static List<Map> getAllAnswer() {
        List<Answer> ra = new ArrayList<Answer>();
        ra = Answer.findAll();
        List<Map> am = new ArrayList<Map>();
        for (Answer ans : ra) {
            am.add(ans.getCompleteAnswer());
        }
        return am;
    }
    
    public static Answer getAnswer(String id){
        Answer q = Answer.findById(id); 
        return q;
    }
    
    
    public static void deleteAnswer(String id) {
        Answer a = Answer.findById(id);
        a.delete();
    }

    public static void createAnswer(String resp, int tipoAnswer, int pregId) {
        Answer ans = new Answer(resp, tipoAnswer, pregId);
        ans.saveIt();
    }
    
    //selecciona las respuestas correspondientes a una pregunta por si id
    public static List<Map> selecAnswer(String pregId){
        List<Answer> preg = Answer.where("pregId = ?", pregId);
        List<Map> sa = new ArrayList<Map>();
        for (Answer p : preg) {
            sa.add(p.getCompleteAnswer());
        }
        return sa;
    }
}