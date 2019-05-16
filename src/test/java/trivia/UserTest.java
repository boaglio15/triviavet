package trivia;

import trivia.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.javalite.activejdbc.Base;

public class UserTest {
	
	@Before
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
      user.set("dni", "");

      assertEquals(user.isValid(), false);
	}
}