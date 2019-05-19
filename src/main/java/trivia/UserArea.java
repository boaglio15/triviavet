
package trivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("users_areas")
public class UserArea extends Model {
    private int userId;
    private int areaId;
    private int completada;
    private int nivel;
    
    public UserArea(){}
    
    public UserArea(int userId, int areaId, int completada, int nivel){
        set("userId", userId);
        set("areaId", areaId);
        set("comletada", completada);
        set("nivel", nivel);
    }
    
    public int getUserId(){
        return this.getInteger("usreId");
    }

    public int getAreaId(){
        return this.getInteger("areaId");
    }
    
    public int getCompletada(){
        return this.getInteger("completada");
    }
    
    public int getNivel(){
        return this.getInteger("nivel");
    }
    
    public Map getCompleteUserArea(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("userId", this.getUserId());
        m.put("areaId", this.getAreaId());
        m.put("completada", this.getCompletada());
        m.put("nivel", this.getNivel());
        return m;
    }
    
    //retorna una lista de las areas donde esta jugando
    public static List<UserArea> getAreasUser(String userId) {
        List<UserArea> aUser = UserArea.where("userId = ?", userId);
        return aUser;
    }

    
    
}
