package triviavet;

import triviavet.Question;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QuestionTest {
	
	@before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/trivia_test?nullNamePatternMatchesAll=true", "root", "root");
    	System.out.println("QuestionTest setup");
    	Base.openTransaction();
  	}

	@After
  	public void after(){
    	System.out.println("QuestionTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
	}

	@Test
	public void invalidQuestion(){
		Question quest = new Question();
		quest.set("preg", "");
		quest.set("areaNombre", "nombre");

		assertEquals(quest.isValid(), false);
	}

	@Test
	public void invalidQuestion2(){
		Question quest = new Question();
		quest.set("preg", "nombreyapellido");
		quest.set("areaNombre", "");

		assertEquals(quest.isValid(), false);
	}

	@Test
	public void validQuestion3(){
		Question quest = new Question();
		quest.set("preg", "nombreyapellido?");
		quest.set("areaNombre", "joseperez");

		assertEquals(quest.isValid(), true);
	}

}



