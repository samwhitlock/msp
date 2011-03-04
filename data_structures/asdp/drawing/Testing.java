package drawing;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** Unit tests for drawing package */
public class Testing {
	/** Run the JUnit tests in the drawing package. */
	public static void main (String[] ignored) {
		try {
			textui.runClasses (drawing.Testing.class);
		} catch (Exception e) {
		}
	}

	@Test public void num1 () throws Exception {
		Number n = new Number (3.0);
		assertEquals (3.0, n.doubleValue (), 0.0);
	}

	@Test public void symbol1() throws Exception {
		String myStr = "testString";
		Symbol sym = new Symbol(myStr);
		assertTrue(sym.stringValue().compareTo(myStr) == 0);
	}

	@Test public void Literal1() throws Exception {
		Literal test = new Literal(3.0);
		assertTrue( test.execute(null).doubleValue() == 3.0);
    }
    
    @Test public void SymbolCommand1() throws Exception {
    	SymbolCommand symCom = new SymbolCommand("test");
    	assertTrue( symCom.execute(null).stringValue().compareTo("test") == 0);
    }
    
    @Test public void SymbolCommand2() throws Exception {
    	SymbolCommand symCom = new SymbolCommand( new Symbol("test"));
    	assertTrue( symCom.execute(null).stringValue().compareTo("test") == 0);
    }
    
    @Test public void AddCommand1() throws Exception {
    	AddCommand test = new AddCommand();
    	test.addCommand(new Literal(6));
    	test.addCommand(new Literal(7));
    	assertTrue( test.execute(null).doubleValue() == 13 );
    }
    
    @Test public void SubtractCommand1() throws Exception {
    	SubtractCommand test = new SubtractCommand();
    	test.addCommand(new Literal(7));
    	test.addCommand(new Literal(6));
    	assertTrue( test.execute(null).doubleValue() == 1);
    }
    
    @Test public void MultiplyCommand1() throws Exception {
    	MultCommand test = new MultCommand();
    	test.addCommand(new Literal(2));
    	test.addCommand(new Literal(10));
    	assertTrue( test.execute(null).doubleValue() == 20);
    }
    
    @Test public void DivideCommand1() throws Exception {
    	DivideCommand test = new DivideCommand();
    	test.addCommand(new Literal(6));
    	test.addCommand(new Literal(3));
    	assertTrue( test.execute(null).doubleValue() == 2);
    }
    
    @Test public void CosCommand1() throws Exception {
    	CosCommand test = new CosCommand();
    	test.addCommand(new Literal(2));
    	assertTrue( test.execute(null).doubleValue() == Math.cos(Math.toRadians(2.0)));
    }
    
    @Test public void SinCommand1() throws Exception {
    	SinCommand test = new SinCommand();
    	test.addCommand(new Literal(3));
    	assertTrue( test.execute(null).doubleValue() == Math.sin(Math.toRadians(3.0)));
    }
    
    @Test public void sqrtCommand1() throws Exception {
    	SquareRootCommand test = new SquareRootCommand();
    	test.addCommand(new Literal(9));
    	assertTrue( test.execute(null).doubleValue() == Math.sqrt(9) );
    }
}
