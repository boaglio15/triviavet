package trivia;

import trivia.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.javalite.activejdbc.Base;

public class UserTest {
	
	@Before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/trivia_test", "root", "root");
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
	
	@Test
	public void invalidateDni() {
		User user = new User();
		user.set("dni","45@4291");
		
		assertEquals(user.isValid(), false);
	}
	
	@Test
	public void nombreNulo() {
		User user = new User();
		user.set("nom", "");
		
		assertEquals(user.isValid(), false);
	}
	
	@Test
	public void apellidoNulo() {
		User user = new User();
		user.set("ape", "");
		
		assertEquals(user.isValid(), false);
	}
	
}