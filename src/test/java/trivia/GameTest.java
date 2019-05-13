package triviavet;

import triviavet.Game;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GameTest {
	
	@before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/trivia_test?nullNamePatternMatchesAll=true", "root", "root");
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
		game.set("fecha", "");

		assertEquals(game.isValid(), false);
	}

	@Test
	public void validGame(){
		Game game = new Game();
		game.set("fecha", "2019-05-06");

		assertEquals(game.isValid(), true);
	}

}