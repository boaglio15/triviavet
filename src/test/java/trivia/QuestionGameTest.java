package triviavet;

import triviavet.QuestionGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QuestionGameTest {
	
	@before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    	System.out.println("QuestionGameTest setup");
    	Base.openTransaction();
  	}

	@After
  	public void after(){
    	System.out.println("QuestionGameTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
	}
  //NOTA: sucede lo mismo que en la clase de test answerGame
}