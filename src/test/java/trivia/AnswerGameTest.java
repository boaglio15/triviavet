package triviavet;

import triviavet.AnswerGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AnswerGameTest {
	
	@before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    	System.out.println("AnswerGameTest setup");
    	Base.openTransaction();
  	}

	@After
  	public void after(){
    	System.out.println("AnswerGameTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
	}

  //NOTA: preguntar si se puede hacer test siendo que la clase posee solamente los id de otras 2 (answer y game).
}