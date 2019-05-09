package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import java.util.*;

@Table("questions_games")
public class QuestionGame extends Model {

        private int questionId;
        private int gameId;

    public QuestionGame(){

    }

    public QuestionGame(int question, int game){
        set("questionId", question);
        set("gameId", game);
    }

    public int getQuestionId(){
        return this.getInteger("questionId");
    }

    public int getGameId(){
        return this.getInteger("gameId");
    }

    public Map getCompleteQuestionGame(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("questionId", this.getQuestionId());
        m.put("gameId", this.getGameId());
        return m;
    }

}
