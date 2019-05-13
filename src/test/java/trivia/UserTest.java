package triviavet;

import triviavet.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UserTest {
	
	@before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/trivia_test?nullNamePatternMatchesAll=true", "root", "root");
    	System.out.println("UserTest setup");
    	Base.openTransaction();
  	}

	@After
  	public void after(){
    	System.out.println("UserTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
	}

	@Test
	public void invalidateUsername(){
      User user = new User();
      user.set("nom", "");

      assertEquals(user.isValid(), false);
	}

	@Test
	public void incorrectUsername(){
		User user = new User();
		user.set("nom", "jose 500"); //NO se permiten espacios
		user.set("pass", "500");

		assertEquals(user.isValid(), false);
	}


	@Test
	public void incorrectUsername2(){
		User user = new User();
		user.set("nom", "jose500");
		user.set("pass", "50!"); //NO se permiten simbolos que no sean alfanumericos

		assertEquals(user.isValid(), false);
	}

	@Test
	public void incorrectUsername3(){
		User user = new User();
		user.set("nom", "jose_500"); //NO se permiten simbolos que no sean alfanumericos
		user.set("pass", "josee50");

		assertEquals(user.isValid(), false);
	}

	@Test
	public void incorrectUsername4(){
		User user = new User();
		user.set("nom", "jose500"); //usuario valido
		user.set("pass", "jose50");

		assertEquals(user.isValid(), true);
	}
}