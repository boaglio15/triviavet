package trivia;

import trivia.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

import org.javalite.activejdbc.Base;

import static org.junit.Assert.assertEquals;

public class GameTest {
	
	@Before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/trivia_test", "root", "root");
    	System.out.println("GameTest setup");
    	Base.openTransaction();
  	}

	@After
  	public void after(){
    	System.out.println("GameTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
	}

	@Test
	public void invalidGame(){
		Game game = new Game();
		//game.set("fecha", "");

		//assertEquals(game.isValid(), false);
	}

	@Test
	public void invalidGame2(){
		Game game = new Game();
		//game.set("fecha", "2019-15-06");

		//assertEquals(game.isValid(), false);
	}

	@Test
	public void validGame(){
		Game game = new Game();
		//game.set("fecha", "2019-05-15");

		//assertEquals(game.isValid(), true);
	}

	@Test
	public void validGame2(){
		Date date = new Date();
		Game game = new Game();
		//game.set("fecha",date);

		assertEquals(game.isValid(), true);
	}
}