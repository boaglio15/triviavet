package trivia.models;

import org.javalite.activejdbc.Model;
import java.util.*;

public class Area extends Model {

    private int nomArea;

    static {
        validatePresenceOf("nomArea").message("Ingrese el nombre del area");
    }

    public Area() {

    }

    public Area(int nomArea) {
        set("area", nomArea);
    }

    public int getNomArea() {
        return this.getInteger("nomArea");
    }


    public Map getCompleteArea() {
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("nomArea", this.getNomArea());
        return m;
    }
    
    public static List<Map> getAllArea() {
        List<Area> r = new ArrayList<Area>();
        r = Area.findAll();
        List<Map> rm = new ArrayList<Map>();
        for (Area area : r) {
            rm.add(area.getCompleteArea());
        }
        return rm;
    }

    public static Area getArea(int  areaId) {
        Area a = Area.findById(areaId); 
        return a; 
    }

   
    public static void createArea(int nombreArea) {
        Area area = new Area(nombreArea);
        area.saveIt();
    }

    public static Map getQuestionArea(Integer areaId){
        List<Question> questions = Question.findBySQL("SELECT * FROM questions WHERE areaId = ?", areaId);
        List<Map> questMap = new ArrayList<Map>();
        for(Question q : questions){
            Map m = new HashMap();
            m.put("idQuest", q.getInteger("id"));
            m.put("preg", q.getString("preg"));
            questMap.add(m);
        }
        Map res = new HashMap();
        res.put("questList", questMap);
        return res;
    }
}