package trivia;

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
}