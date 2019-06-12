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
    public static List<Map> newGame(String userId) {
        List<UserArea> areas = UserArea.getAreasUser(userId);
        List<Map> an = new ArrayList<Map>();
        for (UserArea are : areas) {
            Map m = new HashMap();
            m.put("area", are.getAreaId());
            m.put("nivel", are.getNivel());
            an.add(m);
        }
        return an;
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
    public static int selectQuestionId(List<Integer> pregHechas, List<Integer> pregEnArea) {
        if (pregHechas == null) {
            int pregSelec = (int) (Math.random() * (pregEnArea.size())); //no hay preguntas hechas en el juego para el area, elige una inicial 
            return pregSelec;
        } else {
            int indexRandom = (int) (Math.random() * (pregEnArea.size())); //no hacer pregEnArea.size()-1 xq random no genera el num extremo
            while (pregHechas.contains(pregEnArea.get(indexRandom))) {
                indexRandom = (int) (Math.random() * ((pregEnArea.size())));
            }
            int pregSelec = (int) Question.getQuestion(Integer.toString(pregEnArea.get(indexRandom))).getId();
            return pregSelec;
        }
    }

    //almacena todas las preguntas hechas durante la partida jugada
    //para luego cunado sale del juego con estos datos actualiza la BD
    public static List<Integer> guardarPregHechaPartida(String idPregSelect) {
        List<Integer> pregPartida = new ArrayList<>();
        pregPartida.add(Integer.parseInt((String) idPregSelect));
        return pregPartida;
    }

    //luego de seleccionar una pregunta la agrega a preguntas hechas
    //public static void actualizarPegHechas(int idPregSelect, List<Integer> pregHechas) {
    //    pregHechas.add(idPregSelect); //actualizarPegHechas
    //}

    //luego de seleccionar una pregunta la quita de las preguntas en el area
    //public static void actualizarPregEnAreas(int idPregSelect, List<Integer> pregEnArea) {
    //    pregEnArea.remove(idPregSelect); //actualizarPregEnAreas
    //}

    //selecciona las respuestas correspondientes a una pregunta por si id
    public static List<Answer> selecAnswer(String pregId) {
        List<Answer> preg = Answer.where("pregId = ?", pregId);
        //List<Map> sa = new ArrayList<Map>();
        //for (Answer p : preg) {
        //    sa.add(p.getCompleteAnswer());
        //}
        return preg;
    }
    //FALTA CONTEMPLAR CUANDO SE QUEDA SIN PREGUNTAS EL AREA !!!
    //retorna una pregunta junto con sus respuestas
    public static Map selectQuestionAnswer(List<Integer> pregHechas, List<Integer> pregEnArea) {
        Integer idPregSelect = selectQuestionId(pregHechas, pregEnArea);
        Question pregSelec = Question.getQuestion(Integer.toString(idPregSelect));
        List<Answer> answerSelec = selecAnswer(Integer.toString(idPregSelect));
        Map m = new HashMap();
        m.put("id", pregSelec.getId());
        m.put("areaId", pregSelec.getAreaId());
        m.put("preg", pregSelec.getPreg());
        m.put("resp1", answerSelec.get(0).getResp());
        m.put("tipo1", answerSelec.get(0).getTipoAnswer());
        m.put("resp2", answerSelec.get(1).getResp());
        m.put("tipo2", answerSelec.get(1).getTipoAnswer());
        m.put("resp3", answerSelec.get(2).getResp());
        m.put("tipo3", answerSelec.get(2).getTipoAnswer());
        m.put("resp4", answerSelec.get(3).getResp());
        m.put("tipo4", answerSelec.get(3).getTipoAnswer());
        pregHechas.add(idPregSelect);       //actualizarPegHechas
        pregEnArea.remove(idPregSelect);    //actualizarPregEnAreas
        return m;

    }
    //FALTA CONTEMPLAR CUANDO SE QUEDA SIN PREGUNTAS EL AREA !!!
    //inicializa la partida configurando las preguntas posibles a realiazr, retorna una pregunta junto con sus respuestas
    public static Map selectQuestionAnswerInit(List<Integer> pregHechas, List<Integer> pregEnArea) {
        if (pregHechas != null) { // para caso area ya jugada quita las preguntas realizadas en el area
            for (Integer a : pregHechas) {
                pregEnArea.remove(a);
            }           
        }
        Integer idPregSelect = selectQuestionId(pregHechas, pregEnArea);
        Question pregSelec = Question.getQuestion(Integer.toString(idPregSelect));
        List<Answer> answerSelec = selecAnswer(Integer.toString(idPregSelect));
        Map m = new HashMap();
        m.put("id", pregSelec.getId());
        m.put("areaId", pregSelec.getAreaId());
        m.put("preg", pregSelec.getPreg());
        m.put("resp1", answerSelec.get(0).getResp());
        m.put("tipo1", answerSelec.get(0).getTipoAnswer());
        m.put("resp2", answerSelec.get(1).getResp());
        m.put("tipo2", answerSelec.get(1).getTipoAnswer());
        m.put("resp3", answerSelec.get(2).getResp());
        m.put("tipo3", answerSelec.get(2).getTipoAnswer());
        m.put("resp4", answerSelec.get(3).getResp());
        m.put("tipo4", answerSelec.get(3).getTipoAnswer());
        pregHechas.add(idPregSelect);       //actualizarPegHechas
        pregEnArea.remove(idPregSelect);    //actualizarPregEnAreas
        return m;
    }

    public static void play(List<Integer> pregHechas, List<Integer> pregEnArea, boolean play) {

        if (play) {
            //selectQuestion
            //guardarPregHechaPartida
            //selecAnswer
            //actualizarPegHechas
            //actualizarPregEnAreas

        } else {
            //tomar los datos en guardarPregHechaPartida y pasarlos a la BD

        }
    }

}
