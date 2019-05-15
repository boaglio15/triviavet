package trivia;

import java.util.HashMap;
import java.util.Map;
import org.javalite.activejdbc.Model;

public class Stat extends Model {
   private int userId;
   private int cantCorrectas;
   private int cantIncorrectas;
   
   public Stat(){}
   
   public Stat(int userId){
       set("userId", userId);
   }
   
   public int getUserId(){
       return this.getInteger("userId");
   }
   
   public int getCantCorrectas(){
       return this.getInteger("cantCorrectas");
   }
   
   public int getCantIncorrectas(){
       return this.getInteger("CantIncorrectas");
   }
   
   public Map getCompleteUser() {
        Map m = new HashMap();
        m.put("userId", this.getId());
        m.put("cantCorrectas", this.getCantCorrectas());
        m.put("cantIncorrectas", this.getCantIncorrectas());
        return m;
    }
   
}
