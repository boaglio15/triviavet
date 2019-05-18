package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import java.util.*;

@Table("questions_games")
public class QuestionGame extends Model {

        private int questionId;
        private int gameId;
        private int estado;

    public QuestionGame(){

    }

    public QuestionGame(int question, int game, int estado){
        set("questionId", question);
        set("gameId", game);
        set("estado", estado);
    }

    public int getQuestionId(){
        return this.getInteger("questionId");
    }

    public int getGameId(){
        return this.getInteger("gameId");
    }
    
    public int getEstado(){
        return this.getInteger("estado");
    }

    public Map getCompleteQuestionGame(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("questionId", this.getQuestionId());
        m.put("gameId", this.getGameId());
        m.put("estado", this.getEstado());
        return m;
    }
    
    /*
    //retorna el id de todas las preguntas hechas para un juego dado
    //correctas e incorrectas
    public static List<Integer> allQuestionGame(String gameId) {
        List<QuestionGame> qGames = QuestionGame.where("gameId = ?", gameId);
            List<Integer> listQuestions = new ArrayList<Integer>();
            for (QuestionGame quest : qGames) {
                listQuestions.add(quest.getQuestionId());
            }
        return listQuestions;
    }*/
}