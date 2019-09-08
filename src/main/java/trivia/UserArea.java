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

    public UserArea() {
    }

    public UserArea(int userId, int areaId, int completada, int nivel) {
        set("userId", userId);
        set("areaId", areaId);
        set("completada", completada);
        set("nivel", nivel);
    }

    public int getUserId() {
        return this.getInteger("userId");
    }

    public int getAreaId() {
        return this.getInteger("areaId");
    }

    public int getCompletada() {
        return this.getInteger("completada");
    }

    public int getNivel() {
        return this.getInteger("nivel");
    }

    public Map getCompleteUserArea() {
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

    //retorna una lista (es un solo valor) de los datos del usuario para el area pasada
    public static List<UserArea> getDatAreaUser(String areaId) {
        List<UserArea> datAreaUser = UserArea.where("areaId = ?", areaId);
        return datAreaUser;
    }

    public static void createAreaUser(int userId, int areaId, int completada, int nivel) {
        UserArea userArea = new UserArea(userId, areaId, completada, nivel);
        userArea.saveIt();
    }


    //carga los datos de completada y nivel en caso que el usuario no este cargado o los modifica en caso que ya haya jugado
    public static void updateAreaUser(Integer userId, Integer areaId, Integer completada, Integer nivel) {
        System.out.println("HGGFFFFFGHG");
        List<UserArea> ua = UserArea.where("userId = ? and areaId = ?", userId, areaId);
        if (ua.isEmpty()) {
            createAreaUser(userId, areaId, completada, nivel);
        } else {
            ua.get(0).delete(); //borra el registro de la partida antigua
            UserArea.createAreaUser(userId, areaId, completada, nivel);
        }
    }
}
