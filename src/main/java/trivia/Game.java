package trivia;

import org.javalite.activejdbc.Model;
import java.lang.Math;
import java.util.*;
import java.util.List;

public class Game extends Model {

    /*
    static {
        dateFormat("yyyy-MM-dd", "fecha"); //forma de la fecha 'YYYY-MM-DD'
        validatePresenceOf("fecha").message("Ingrese la fecha del juego");
    }*/

    private int userId;
    // private Date fecha;

    public Game() {
    }

    public Game(int idUsuario) {
        set("userId", idUsuario);
        // set("fecha", fecha);
    }

    public int getUserId() {
        return this.getInteger("userId");
    }

    /*public Date getFecha() {
        return this.getDate("fecha");
    }*/
    public Map getCompleteGame() {
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("userId", this.getUserId());
        //m.put("fecha", this.getFecha());
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

    public static void createGame(Integer userId) { // Date gameFecha
        Game game = new Game(userId);
        game.saveIt();
    }

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

    //------metodos para manejo de juego-----------------//
    //retorna el id de todas las preguntas hechas para un juego dado correctas e incorrectas
    public static List<Integer> allQuestionGame(String gameId) {
        List<QuestionGame> qGames = QuestionGame.where("gameId = ?", gameId);
        List<Integer> listQuestions = new ArrayList<Integer>();
        for (QuestionGame quest : qGames) {
            listQuestions.add(quest.getQuestionId());
        }
        return listQuestions;
    }

    //retorna el id de todas las preguntas hechas en un juego para un area determinada
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

    //determina la cantidad de preg contestadas correctamente en un area
    public static int getCantQuestCorrecArea(String userId, List<Integer> pregHechasEnArea) {
        if (pregHechasEnArea.isEmpty()) {
            return 0;
        }
        int cantRespCorrect = 0;
        System.out.println(pregHechasEnArea.toString());
        for (Integer pregId : pregHechasEnArea) {
            QuestionGame qg = QuestionGame.getEstadosQuestGame(Integer.toString(pregId), userId);
            if ((Integer) qg.getEstado() == 1) {
                cantRespCorrect++;
            }
        }
        return cantRespCorrect;
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

    //selecciona las respuestas correspondientes a una pregunta por si id
    public static List<Answer> selecAnswer(String pregId) {
        List<Answer> preg = Answer.where("pregId = ?", pregId);
        //List<Map> sa = new ArrayList<Map>();
        //for (Answer p : preg) {
        //    sa.add(p.getCompleteAnswer());
        //}
        return preg;
    }

    //det el nivel donde esta jugando en un area
    private static Integer nivel(int cantPregCorrectArea) { //la cant de preg necesarias para avanzar puede cambiarse
        if (cantPregCorrectArea <= 1) {
            return 1;
        };
        if (cantPregCorrectArea > 1 && cantPregCorrectArea <= 2) {
            return 2;
        };
        if (cantPregCorrectArea > 2 && cantPregCorrectArea <= 3) {
            return 3;
        };
        return -1;
    }

    public static List<Answer> aleatoryOptionsQuestion(List<Answer> answers) {
        int numRandom;
        List<Answer> resultList = new LinkedList<Answer>();
        Answer auxiliar;
        for (int i = 4; i > 1; i--) {
            numRandom = (int) (Math.random() * i);
            auxiliar = answers.remove(numRandom);
            resultList.add(auxiliar);
        }
        resultList.add(answers.get(0));
        return resultList;
    }

    //inicializa la partida configurando: 
    // -verifica que en el area haya preg para hacer 
    // -verifica si el area esta completada
    // -det el nivel en que esta jugando en el area
    // -determina la cantidad depreguntas correctas hechas por el jugador en el area
    // -selecciona una preg a realizar
    // -selecciona las respuestas posibles a la preg seleccionada
    public static Map selectQuestionAnswerInit(String areaId, List<Integer> pregHechas, List<Integer> pregEnArea, int cantPregIncorrect) {
        List<UserArea> datAreaUser = UserArea.getDatAreaUser(areaId);   //det los datos de nivel y completada para el area elegida por el usuario
        if (datAreaUser.isEmpty()) {                                                    //caso jugador nuevo en el area seleccionada o que nunca jugo
            Integer idPregSelect = selectQuestionId(pregHechas, pregEnArea);            //det en forma random el id de una preg a realizar
            Question pregSelec = Question.getQuestion(Integer.toString(idPregSelect));  //obtiene la preg
            List<Answer> answerSelec = selecAnswer(Integer.toString(idPregSelect));
            List<Answer> answerSelecFinal = aleatoryOptionsQuestion(answerSelec);
            Map m = new HashMap();                                                      //mapea toda la info a enviar
            m.put("id", pregSelec.getId());
            m.put("areaId", pregSelec.getAreaId());
            m.put("preg", pregSelec.getPreg());
            m.put("resp1", answerSelecFinal.get(0).getResp());
            m.put("tipo1", answerSelecFinal.get(0).getTipoAnswer());
            m.put("resp2", answerSelecFinal.get(1).getResp());
            m.put("tipo2", answerSelecFinal.get(1).getTipoAnswer());
            m.put("resp3", answerSelecFinal.get(2).getResp());
            m.put("tipo3", answerSelecFinal.get(2).getTipoAnswer());
            m.put("resp4", answerSelecFinal.get(3).getResp());
            m.put("tipo4", answerSelecFinal.get(3).getTipoAnswer());
            m.put("areaSinPreg", 0);
            m.put("areaComplet", 0);
            m.put("nivel", 1);
            m.put("cantPregInco", 0);
            pregHechas.add(idPregSelect);                                                 //actualizarPegHechas
            pregEnArea.remove(idPregSelect);                                              //actualizarPregEnAreas
            return m;
        } else {                                           //caso jugador con partidas jugadas
            if (datAreaUser.get(0).getCompletada() == 1) { //caso area completada
                Map m = new HashMap();
                m.put("areaSinPreg", 0);
                m.put("areaComplet", 1);
                return m;
            } else {
                if (pregHechas.size() == pregEnArea.size()) { //caso  sin preguntas para hacer 
                    Map m = new HashMap();
                    m.put("areaSinPreg", 1);
                    m.put("areaComplet", 0);
                    return m;
                } else {
                    if (pregHechas != null) {                                      // para caso area ya jugada quita las preguntas realizadas en el area
                        for (Integer a : pregHechas) {
                            pregEnArea.remove(a);
                        }
                    }
                    Integer idPregSelect = selectQuestionId(pregHechas, pregEnArea);            //det en froma random el id de una preg a realizar
                    Question pregSelec = Question.getQuestion(Integer.toString(idPregSelect));  //obtiene la preg
                    List<Answer> answerSelec = selecAnswer(Integer.toString(idPregSelect));     //det las respuestas para la preg
                    List<Answer> answerSelecFinal = aleatoryOptionsQuestion(answerSelec);
                    Map m = new HashMap();                                                      //mapea toda la info a inviar
                    m.put("id", pregSelec.getId());
                    m.put("areaId", pregSelec.getAreaId());
                    m.put("preg", pregSelec.getPreg());
                    m.put("resp1", answerSelecFinal.get(0).getResp());
                    m.put("tipo1", answerSelecFinal.get(0).getTipoAnswer());
                    m.put("resp2", answerSelecFinal.get(1).getResp());
                    m.put("tipo2", answerSelecFinal.get(1).getTipoAnswer());
                    m.put("resp3", answerSelecFinal.get(2).getResp());
                    m.put("tipo3", answerSelecFinal.get(2).getTipoAnswer());
                    m.put("resp4", answerSelecFinal.get(3).getResp());
                    m.put("tipo4", answerSelecFinal.get(3).getTipoAnswer());
                    m.put("areaSinPreg", 0);
                    m.put("areaComplet", 0);
                    m.put("nivel", datAreaUser.get(0).getNivel());
                    m.put("cantPregInco", cantPregIncorrect);
                    pregHechas.add(idPregSelect);                                                 //actualizarPegHechas
                    pregEnArea.remove(idPregSelect);                                              //actualizarPregEnAreas
                    return m;
                }
            }
        }
    }
    
        //retorna una pregunta junto con sus respuestas y demas datos
    public static Map selectQuestionAnswer(String userId, List<Integer> pregHechas, List<Integer> pregEnArea, int cantPregCorr, int cantPregIncorrect) {
        Integer nivel = nivel(cantPregCorr);
        System.out.println("NIVEL " + nivel);
        Integer areaComplet = 0;
        if (nivel == -1) {
            areaComplet = 1;
            Map m = new HashMap();
            m.put("nivel", nivel);
            m.put("areaComplet", areaComplet);
            return m;
        }
        if (pregEnArea.isEmpty()) { //caso donde no hay mas preguntas en el area para hacer
            Map m = new HashMap();
            m.put("nivel", nivel);
            m.put("areaSinPreg", 1);
            return m;
        }
        System.out.println("AREA COMPLETA " + areaComplet);
        //ver caso cuando queda una preg y esa preg se contesta bien con lo que pasa de nivel o completa el area
        if (pregEnArea.size() == 1) {//caso en que queda una pregunta para hacer, la cual se hace y queda vacia las preg en area
            System.out.println("NO HAY MAS PREG EN EL AREA");
            Integer idPregSelect = selectQuestionId(pregHechas, pregEnArea);
            Question pregSelec = Question.getQuestion(Integer.toString(idPregSelect));
            List<Answer> answerSelec = selecAnswer(Integer.toString(idPregSelect));
            List<Answer> answerSelecFinal = aleatoryOptionsQuestion(answerSelec);
            Map m = new HashMap();
            m.put("id", pregSelec.getId());
            m.put("areaId", pregSelec.getAreaId());
            m.put("preg", pregSelec.getPreg());
            m.put("resp1", answerSelecFinal.get(0).getResp());
            m.put("tipo1", answerSelecFinal.get(0).getTipoAnswer());
            m.put("resp2", answerSelecFinal.get(1).getResp());
            m.put("tipo2", answerSelecFinal.get(1).getTipoAnswer());
            m.put("resp3", answerSelecFinal.get(2).getResp());
            m.put("tipo3", answerSelecFinal.get(2).getTipoAnswer());
            m.put("resp4", answerSelecFinal.get(3).getResp());
            m.put("tipo4", answerSelecFinal.get(3).getTipoAnswer());
            m.put("sinPregArea", 1);       //se realiza la ultima preg y no hay mas en el area (1=true) 
            m.put("areaComplet", areaComplet);
            m.put("nivel", nivel);
            m.put("cantPregInco", cantPregIncorrect);
            pregHechas.add(idPregSelect);       //actualizarPegHechas
            pregEnArea.remove(idPregSelect);    //actualizarPregEnAreas
            return m;
        } else {                                //caso hay preg para hacer
            Integer idPregSelect = selectQuestionId(pregHechas, pregEnArea);
            Question pregSelec = Question.getQuestion(Integer.toString(idPregSelect));
            List<Answer> answerSelec = selecAnswer(Integer.toString(idPregSelect));
            List<Answer> answerSelecFinal = aleatoryOptionsQuestion(answerSelec);
            Map m = new HashMap();
            m.put("id", pregSelec.getId());
            m.put("areaId", pregSelec.getAreaId());
            m.put("preg", pregSelec.getPreg());
            m.put("resp1", answerSelecFinal.get(0).getResp());
            m.put("tipo1", answerSelecFinal.get(0).getTipoAnswer());
            m.put("resp2", answerSelecFinal.get(1).getResp());
            m.put("tipo2", answerSelecFinal.get(1).getTipoAnswer());
            m.put("resp3", answerSelecFinal.get(2).getResp());
            m.put("tipo3", answerSelecFinal.get(2).getTipoAnswer());
            m.put("resp4", answerSelecFinal.get(3).getResp());
            m.put("tipo4", answerSelecFinal.get(3).getTipoAnswer());
            m.put("sinPregArea", 0); //0 = false
            m.put("areaComplet", areaComplet);
            m.put("nivel", nivel);
            m.put("cantPregInco", cantPregIncorrect);
            pregHechas.add(idPregSelect);       //actualizarPegHechas
            pregEnArea.remove(idPregSelect);    //actualizarPregEnAreas
            return m;
        }
    }

    //-----------------fin metodos para manejo de juego-----------------//
}
