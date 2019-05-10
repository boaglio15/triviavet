package triviavet;

import triviavet.Area;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AreaTest {
	
	@before
	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    	System.out.println("AreaTest setup");
    	Base.openTransaction();
  	}

	@After
  	public void after(){
    	System.out.println("AreaTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
	}

	@Test
	public void invalidateNameArea(){
      Area area = new Area();
      area.set("nombreArea", "");

      assertEquals(area.isValid(), false);
	}

  @Test //solo se aceptan caracteres alfa-numericos
  public void invalidateNameArea2(){
      Area area = new Area();
      ares.set("nombreArea", "@@@@quimica");

      assertEquals(area.isValid(), false);
  }
}