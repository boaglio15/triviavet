package trivia;

import org.javalite.activejdbc.Model;
import java.util.*;

public class Answer extends Model {

	private String resp;
    private int	tipoAnswer; 
    private int	pregId;

    public Answer(){

    }

    public Answer(String respuesta, int tipoRespuesta, int idPregunta){
        set("resp", respuesta);
        set("tipoAnswer", tipoRespuesta);
        set("pregId", idPregunta);
    }

    public String getResp(){
        return this.getString("resp");
    }

    public int getTipoAnswer(){
        return this.getInteger("tipoAnswer");
    }

    public int getPregId(){
    	return this.getInteger("pregId");
    }

    public Map getCompleteAnswer(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("resp", this.getResp());
        m.put("tipoAnswer", this.getTipoAnswer());
        m.put("pregId". this.getPregId());
        return m;
    }
}