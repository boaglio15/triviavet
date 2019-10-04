package trivia;

import trivia.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.javalite.activejdbc.Base;

import static org.junit.Assert.assertEquals;

public class AnswerTest {
	
	@Before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/trivia_test", "admin", "password");
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
}
