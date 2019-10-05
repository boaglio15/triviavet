package trivia;

import trivia.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.javalite.activejdbc.Base;

public class QuestionTest {
	
	@Before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/trivia_test", "admin", "password");
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

		assertEquals(quest.isValid(), false);
	}
}



