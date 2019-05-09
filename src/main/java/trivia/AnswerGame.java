package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import java.util.*;

@Table("answers_games")
public class AnswerGame extends Model {

        private int answerId;
        private int gameId;

    public AnswerGame(){

    }

    public AnswerGame(int answer, int game){
        set("answerId", answer);
        set("gameid", game);
    }

    public int getAnswerId(){
        return this.getInteger("answerId");
    }

    public int getGameId(){
        return this.getInteger("gameId");
    }

    public Map getCompleteAnswerGame(){
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("answerId", this.getAnswerId());
        m.put("gameId", this.getGameId());
        return m;
    }
}
