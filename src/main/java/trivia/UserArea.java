
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
    
    public UserArea(){}
    
    public UserArea(int userId, int areaId, int completada){
        set("userId", userId);
        set("areaId", areaId);
        set("comletada", completada);
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
    
    public Map getCompleteUserArea(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("userId", this.getUserId());
        m.put("areaId", this.getAreaId());
        m.put("completada", this.getCompletada());
        return m;
    }
    
    //retorna las areas donde esta jugando un usuario
    public static List<Integer> getAreasUser(String userId) {
        List<UserArea> aUser = UserArea.where("userId = ?", userId);
            List<Integer> listArea = new ArrayList<Integer>();
            for (UserArea area : aUser) {
                listArea.add(area.getAreaId());
            }
        return listArea;
    }

    
    
}
