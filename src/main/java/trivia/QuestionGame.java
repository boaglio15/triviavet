package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import java.util.*;

@Table("questions_games")
public class QuestionGame extends Model {

    private int questionId;
    private int gameId;
    private int estado;

    public QuestionGame() {

    }

    public QuestionGame(int question, int game, int estado) {
        set("questionId", question);
        set("gameId", game);
        set("estado", estado);
    }

    public int getQuestionId() {
        return this.getInteger("questionId");
    }

    public int getGameId() {
        return this.getInteger("gameId");
    }

    public int getEstado() {
        return this.getInteger("estado");
    }

    public Map getCompleteQuestionGame() {
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("questionId", this.getQuestionId());
        m.put("gameId", this.getGameId());
        m.put("estado", this.getEstado());
        return m;
    }

    public static void createQuestionGame(int questionId, int gameId, int estado) {
        QuestionGame questionGame = new QuestionGame(questionId, gameId, estado);
        questionGame.saveIt();
    }

    //det el estado (correcto o incorrecto) de una preg en un juego dado
    public static QuestionGame getEstadosQuestGame(String pregId, String gameId) {
        List<QuestionGame> q = QuestionGame.where("questionId = ? and gameId = ?", pregId, gameId);
        System.out.println("questId game Id estado: " + q);
        return q.get(0);
    }

    public static void updateQuestionGame(String userId, int indicePregHechas, List<Integer> pregHechas, List<Integer> respHechasCorIncor) {
        for (int i = indicePregHechas; i < pregHechas.size(); i++) { //empieza a recorrer sin considerar las preg hechas en otra partida
            QuestionGame.createQuestionGame(pregHechas.get(i), Integer.parseInt(userId), respHechasCorIncor.get(i - indicePregHechas));
        } //consid que las lista pregHechas y respHechasCorIncor son de dist long por eso se toman los indices asi 
    }

}
