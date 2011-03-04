package reversigame;

import org.junit.Test;
import static org.junit.Assert.*;
import static reversigame.PieceColor.*;
import ucb.junit.textui;
import static reversigame.Command.*;

/** Central dispatching point for all testing.
 * @author Sam Whitlock (cs61b-eo)
 *  */
public class Testing {

	/** Run the JUnit tests in the reversigame package.
	 * @author Sam Whitlock (cs61b-eo)
	 * */
	public static void main (String[] ignored) {
		textui.runClasses (reversigame.Testing.class);
	}

	/**Testing basic functionality of the board
	 * Full functionality of this class is more suitably tested 
	 * through the regression tests in the testing directory
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	@Test public void BoardBasic() {
		Board brd = new Board();
		assertEquals(brd.whoseMove(), BLACK);
		assertEquals(brd.numMoves(), 0);
		assertTrue(brd.blackPieces() == 2 && brd.whitePieces() == 2);
		assertTrue(!brd.gameOver());
		brd.resign();
		assertTrue(brd.resigned());
		brd.clear();
		assertTrue(brd.whoseMove() == BLACK);
		assertTrue(brd.makeMove("d3"));
		assertEquals(brd.whoseMove(), WHITE);
		assertEquals(brd.numMoves(), 1);
		assertEquals(brd.whitePieces(), 1);
		assertEquals(brd.blackPieces(), 4);
	}

	/**Testing basic functionality of the AI player
	 * Full functionality of this class is more suitably tested 
	 * through the regression tests in the testing directory
	 * This basically just checks one move into a board to make sure 
	 * that the AI does not make illegal moves. Again, more in depth 
	 * testing is done through regression tests in the testing directory.
	 * @param void
	 * @return void
	 * @author Sam Whitlock (cs61b-eo)
	 */
	@Test public void AIBasic() {
		Board b = new Board();
		AI ai = new AI(b, WHITE);
		b.makeMove("d3");
		ai.takeMove();
		assertFalse(b.lastMove().compareTo("d3") == 0);
	}

	/**Tests of the Command class and its proper parsing
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	@Test public void CommandParsing() {
		String[] opnd = new String[2];
		assertTrue(AUTO == Command.parseCommand("auto", opnd));
		assertTrue(COLOR == Command.parseCommand("cOLor wHiTE", opnd) && opnd[0].compareToIgnoreCase("white") == 0);
		assertTrue(COLOR == Command.parseCommand("COloR BLAcK", opnd) && opnd[0].compareToIgnoreCase("black") == 0 );
		assertTrue(RESIGN == Command.parseCommand("rESigN", opnd));
		assertTrue(SEED == Command.parseCommand("sEEd", opnd));
		assertTrue(QUIT == Command.parseCommand("QUit", opnd));
		assertTrue(DUMP == Command.parseCommand("DuMp", opnd));
		assertTrue(AUTO == Command.parseCommand("AUTo", opnd));
		assertTrue(CLEAR == Command.parseCommand("CLeAr", opnd));
		assertTrue(MANUAL == Command.parseCommand("mANUal", opnd));
		assertTrue(START == Command.parseCommand("StaRT", opnd));
		assertTrue(HELP == Command.parseCommand("HELp", opnd));
		assertTrue(PASS == Command.parseCommand("-", opnd));
		assertTrue(LOAD == Command.parseCommand("load myTest.test", opnd) && opnd[0].compareTo("myTest.test") == 0);
		assertTrue(HOST == Command.parseCommand("host letter428241953981358971365________5938735879wsjhk", opnd) && opnd[0].compareTo("letter428241953981358971365________5938735879wsjhk") == 0);
		assertTrue(JOIN == Command.parseCommand("join lizard193_monkey998@google.net", opnd) && opnd[0].compareTo("lizard193_monkey998") == 0 && opnd[1].compareTo("google.net") == 0);
		assertTrue(PIECEMOVE == Command.parseCommand("d8", opnd) && opnd[0].compareTo("d8") == 0);
		assertTrue(ERROR == Command.parseCommand("a11", opnd));
		assertTrue(ERROR == Command.parseCommand("j5", opnd));
		assertTrue(ERROR == Command.parseCommand("this should error cuz it don't fit nothin' 135093109135&^241867241", opnd));
	}

	/**Tests for the char function for the PieceColor enum
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	@Test public void PieceColorChar() {
		assertEquals(EMPTY.pieceChar().compareTo("-"), 0);
		assertEquals(WHITE.pieceChar().compareTo("w"), 0);
		assertEquals(BLACK.pieceChar().compareTo("b"), 0);
	}

	/**Tests for the Opposite function for the PieceColor enum
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	@Test public void PieceColorOpposite() {
		assertTrue(WHITE.opposite() == BLACK);
		assertTrue(BLACK.opposite() == WHITE);
	}

	/**Tests for the AsString function for the PieceColor enum
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	@Test public void PieceColorAsString() {
		assertEquals(BLACK.asString().compareTo("black"), 0);
		assertEquals(WHITE.asString().compareTo("white"), 0);
		assertEquals(EMPTY.asString().compareTo(""), 0);
	}

	/**Tests for the Position class
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	@Test public void Position() {
		Position p = new Position(13, 99);
		assertEquals(p.position[0], 13);
		assertEquals(p.position[1], 99);
		Position q = new Position(13, 99);
		assertTrue(p.equals(q));
	}
}
