package triviavet;

import triviavet.Answer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AnswerTest {
	
	@before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/trivia_test?nullNamePatternMatchesAll=true", "root", "root");
    	System.out.println("AnswerTest setup");
    	Base.openTransaction();
  	}

	@After
  	public void after(){
    	System.out.println("AnswerTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
	}

	@Test
	public void invalidAnswer(){
		Answer answer = new Answer();
		answer.set("resp", "");

		assertEquals(answer.isValid(), false);
	}

	@Test //las respuestas deben ser alfa-numericas
	public void invalidAnswer(){
		Answer answer = new Answer();
		answer.set("resp", "hola$$$");

		assertEquals(answer.isValid(), false);
	}


	@Test //el tipo de respuesta es valido si es 0 o 1
	public void validTypeAnswer(){
		Answer answer = new Answer();
		answer.set("tipoAnswer", "1");

		assertEquals(answer.isValid(), true);
	}

	@Test
	public void validTypeAnswer(){
		Answer answer = new Answer();
		answer.set("tipoAnswer", "0");

		assertEquals(answer.isValid(), true);
	}


}
