package trivia;

import org.javalite.activejdbc.Model;
import java.util.*;

public class Game extends Model {

        private int userId;
        private Date fecha;

    public Game(){

    }

    public Game(int idUsuario, Date dia){
        set("userId", idUsuario);
        set("fecha", fecha);
    }

    public int getUserId(){
        return this.getInteger("userId");
    }

    public Date getFecha(){
        return this.getDate("fecha");
    }

    public Map getCompleteGame(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("userId", this.getUserId());
        m.put("fecha", this.getFecha());
        return m;
    }

    public String toString(){
        return "u: " + userId + "f: " + fecha;
    }



}
