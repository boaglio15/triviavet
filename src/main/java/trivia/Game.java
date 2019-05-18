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
    //
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
    public static ArrayList<Integer> allQuestionArea(String areaId) {
        List<Question> quest = Question.where("areaId = ?", areaId);
        ArrayList<Integer> listQuestions = new ArrayList<Integer>();
        for (Question questA : quest) {
            listQuestions.add((Integer) questA.getId());
        }
        return listQuestions;
    }
    
    //retorna el id de una pregunta para hacer en forma random
    public static int selectQuestion(List<Integer> pregHechas, List<Integer> pregEnArea) {      
        int indexRandom = (int)(Math.random()*(pregEnArea.size())); //no hacer pregEnArea.size()-1 xq random no genera el num extremo
        while (pregHechas.contains(pregEnArea.get(indexRandom))) {
            indexRandom = (int)(Math.random()*((pregEnArea.size()-1)));
        }
        int pregSelec = (int) Question.getQuestion(Integer.toString(pregEnArea.get(indexRandom))).getId();
        return pregSelec; 
    }
    
    //almacena todas las preguntas hechas durante la partida jugada 
    //para luego cunado sale del juego con estos datos actualiza la BD
    private List<Integer> guardarPregHechaPartida(String idPregSelect){
        List<Integer> pregPartida = new ArrayList<>();
        pregPartida.add(Integer.parseInt((String) idPregSelect));
        return pregPartida;
    }
    
    //luego de seleccionar una pregunta la agrega a preguntas hechas
    private void actualizarPegHechas(int idPregSelect, List<Integer> pregHechas){
        pregHechas.add(idPregSelect);
    }
    
    //luego de seleccionar una pregunta la quita de las preguntas en el area
    private void actualizarPregEnAreas(int idPregSelect, List<Integer> pregEnArea){
        pregEnArea.remove(idPregSelect);
    }

    //selecciona las respuestas correspondientes a una pregunta por si id
    public static List<Map> selecAnswer(String pregId){
        List<Answer> preg = Answer.where("pregId = ?", pregId);
        List<Map> sa = new ArrayList<Map>();
        for (Answer p : preg) {
            sa.add(p.getCompleteAnswer());
        }
        return sa;
    }
    
    public static void play(List<Integer> pregHechas, List<Integer> pregEnArea, boolean play) {
        
        if (play) {
            //selectQuestion
            //guardarPregHechaPartida
            //selecAnswer
            //actualizarPegHechas
            //actualizarPregEnAreas
            
        }else {
            //tomar los datos en guardarPregHechaPartida y pasarlos a la BD
        
        }
    }

    
    
}
