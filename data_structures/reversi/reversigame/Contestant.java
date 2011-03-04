package reversigame;

/** A generic Reversi player, whose job is to choose moves.
 * @author Sam Whitlock (cs61b-eo)
 */
abstract class Contestant {

	/** A Contestant that will play MYCOLOR on BOARD. */
	Contestant (Board board, PieceColor myColor) {
		this.board = board;
		this.myColor = myColor;
	}

	PieceColor myColor () {
		return myColor;
	}

	/** Make my next move on the current board.  Assumes that 
	 *  board.whoseMove() == myColor and that !board.gameOver (). */
	abstract void takeMove ();

	protected Board board;
	protected final PieceColor myColor;
}