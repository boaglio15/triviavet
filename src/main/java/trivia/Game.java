package trivia;

import org.javalite.activejdbc.Model;
import java.util.*;
import java.util.List;


public class Game extends Model {

    static {
        dateFormat("yyyy-MM-dd", "fecha"); //forma de la fecha 'YYYY-MM-DD'
        validatePresenceOf("fecha").message("Ingrese la fecha del juego");
    }

    private int userId;
    private Date fecha;

    public Game() {
    }

    public Game(int idUsuario, Date dia) {
        set("userId", idUsuario);
        set("fecha", fecha);
    }

    public int getUserId() {
        return this.getInteger("userId");
    }

    public Date getFecha() {
        return this.getDate("fecha");
    }

    public Map getCompleteGame() {
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

    //------metodos para manejo de juego-----------------
    
    //este metodo tiene que traer todos los datos de juego del usuario
    public static Map newGame(String userId) {
        List<Integer> areas = UserArea.getAreasUser(userId);
        Map m = new HashMap();
        m.put("nivel", User.getUser(userId).getNivel());
        for (Integer are : areas) {
            m.put("area", are);
        }
        return m;
    }

    //retorna el id de todas las preguntas hechas para un juego dado
    //correctas e incorrectas
    public static List<Integer> allQuestionGame(String gameId) {
        List<QuestionGame> qGames = QuestionGame.where("gameId = ?", gameId);
            List<Integer> listQuestions = new ArrayList<Integer>();
            for (QuestionGame quest : qGames) {
                listQuestions.add(quest.getQuestionId());
            }
        return listQuestions;
    }
    
    //retorna el id de todas las preguntas hechas en un juego para un area determinada
    //el user de todas areas en las que esta jugando selecciona una para seguir jugando
    public static List<Integer> getAllQuestionGameArea(String gameId, String areaId) {
        List<Integer> questId = new ArrayList<Integer>();
        List<Integer> qId = allQuestionGame(gameId);
        for (Integer quest : qId) {
            Question qq = Question.getQuestion(Integer.toString(quest));
            if ((Integer) qq.getAreaId() == Integer.parseInt((String) areaId)) {
                questId.add((int) qq.getId());
            }
        }
        return questId;
    }
    
    //retorna el id de todas las preguntas en un area
    public static List<Integer> allQuestionArea(String areaId) {
        List<Question> quest = Question.where("areaId = ?", areaId);
        List<Integer> listQuestions = new ArrayList<Integer>();
        for (Question questA : quest) {
            listQuestions.add((Integer) questA.getId());
        }
        return listQuestions;
    }
    
    
    //REVISAR ESTE METODO LA FORMA EN QUE SELECCIONA ¡¡¡
    //retorna una pregunta para hacer
    public static String selectQuestion(List<Integer> pregHechas, List<Integer> pregEnArea) {
        for (Integer questId : pregHechas) {
            String id = "4"; //considerando las preguntas que hay en un area selccionar unu id  
            ///
            Question pregSelec = Question.getQuestion(id);
            if (questId != pregSelec.getId()) {
                return pregSelec.getPreg();
            }
        }
        return null;
    }
    


    public static void play(String userId) {

    }

    
    
}
