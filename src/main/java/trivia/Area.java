package trivia;

import org.javalite.activejdbc.Model;
import java.util.*;

public class Area extends Model {
	
	private int nombreArea;
    private int userId;

    public Area(){

    }

    public Area(String area, int idUsuario){
        set("nombreArea", area);
        set("userId", idUsuario);
    }

    public int getNombreArea(){
        return this.getInteger("userId");
    }

    public int getUserId(){
        return this.getString("nombreArea");
    }

    public Map getCompleteArea(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("nombreArea", this.getNombreArea());
        m.put("userId", this.getUserId());
        return m;
    }
}
