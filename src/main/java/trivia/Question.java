package trivia;

import org.javalite.activejdbc.Model;
import java.util.*;

public class Question extends Model {

	private String preg;
    private	int areaId;
    private int userId;
    private int correcta;

    public Question(){

    }

    public Question(String pregunta, int idArea, int idUsuario, int correct){
        set("preg", pregunta);
        set("areaId", idArea);
        set("userId", idUsuario);
        set("correcta", correct);
    }

    public String getPreg(){
        return this.getString("preg");
    }

    public int getAreaId(){
        return this.getInteger("areaId");
    }

    public int getUserId(){
    	return this.getInteger("userId");
    }

    public int getCorrecta(){
    	return this.getInteger("correcta");
    }

    public Map getCompleteQuestion(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("preg", this.getPreg());
        m.put("areaId", this.getAreaId());
        m.put("userId", this.getUserId());
        m.put("correcta", this.getCorrecta());
        return m;
    }
}
