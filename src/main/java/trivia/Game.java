package trivia;

import org.javalite.activejdbc.Model;
import java.util.*;

public class Game extends Model {

        private int userId;
        private Date fecha;

    public Game(){}

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
    
    public static List<Map> getAllGames() {
        List<Game> r = new ArrayList<Game>();
        r = Game.findAll();
        List<Map> rm = new ArrayList<Map>();
        for (Game game : r) {
            rm.add(game.getCompleteGame());
        }
        return rm;
    }
   
    public static Game getGame(String id) {
        Game l = Game.findById(id);
        return l;
    }

    public static void deleteGame(String id) {
        Game g = Game.findById(id);
        g.delete();
    }

    public static void createGame(Integer userId, Date gameFecha) {
        Game game = new Game(userId, gameFecha);
        game.saveIt();
    }

    //este metodo tiene que traer todos los datos de juego del usuario
    public static Map newGame(String userId){
        User user = User.getUser(userId);
        Map m = new HashMap();
        m.put("nivel", user.getNivel());
        m.put("idArea", user.getIdArea());
        return m;
    }
    
    public static void play(String userId){
        
        
    }

}