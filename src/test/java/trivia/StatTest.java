package triviavet;

import triviavet.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StatTest {
	
	@before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    	System.out.println("StatTest setup");
    	Base.openTransaction();
  	}

	@After
  	public void after(){
    	System.out.println("StatTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
	}

	@Test //la cantidad de resp correctas no puede ser negativa
	public void invalidStat(){
		Stat stat = new Stat();
		stat.set("cantCorrectas", "-1");

		assertEquals(stat.isValid(), false);
	}

	@Test //la cantidad de respuestas icorrectas no puede ser negativo
	public void invalidStat2(){
		Stat stat = new Stat();
		stat.set("cantIncorrectas", "-2");

		assertEquals(stat.isValid(), false);
	}
}