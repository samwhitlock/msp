package reversigame;

import static reversigame.Command.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/** A Contestant that receives its moves from the User.
 * @author Sam Whitlock (cs61b-eo)
 */
class Human extends Contestant {

	/** A Contestant that will play MYCOLOR on BOARD, taking its move from
	 *  USER. */
	Human (Controller user, Board board, PieceColor myColor) {
		super (board, myColor);
		this.controller = user;
	}

	/** Assuming that the game is not over and it is my turn, make a
	 *  valid move on the board.  (Note: never tries to make an illegal move.
	 *  The user is prompted until he gets it right.) */      
	void takeMove () {
		if( board.whoseMove() == myColor ) {
			String line = controller.getLine ();
			String[] opnd = new String[2];
			Command c = Command.parseCommand(line, opnd);

			if(c==AUTO) {
				System.err.println("error : auto is not a legal command during gameplay");
			} else if(c==CLEAR) {
				System.err.println("error : clear is not a legal command during gameplay");
			} else if(c==SEED) {
				System.err.println("error : cannot seed AI during gameplay");
			} else if(c==START) {
				System.err.println("error : you are already in a game");
			} else if(c==JOIN) {
				System.err.println("error : you cannot join a new game at this time");
			} else if(c==HOST) {
				System.err.println("error : you cannot host a new game at this time");
			} else if(c==PASS) {
				if( !board.pass() ) {
					System.err.println("error : you cannot pass from this board");
				}
			} else if(c==PIECEMOVE) {
				if( !board.makeMove(opnd[0]) ) {
					System.err.println("error : bad move");
				}
			} else if(c==MANUAL) {
				System.err.println("error : you are already playing in this mode");
			} else if(c==MOVES) {
				Controller.printMoves(board.legalMoves());
			} else if(c==COLOR) {
				System.err.println("error : you cannot change your color during gameplay");
			} else if(c==RESIGN) {
				board.resign();
			} else if(c==LOAD) {
				try {
					controller.prepend(new BufferedReader(new FileReader(opnd[0])));
					System.out.println("File loading....");
				} catch (FileNotFoundException e) {
					System.err.println("error : file not found");
				}
			} else if(c==HELP) {
				Controller.printHelp();
			} else if(c==DUMP) {
				Controller.dump(board);
			} else if(c==QUIT) {
				board.resign();
				controller.quitnow = true;
			} else if(c==PRINT) {
				Controller.printBoard(board);
			} else {
				System.err.println("error : bad input");
			}
		}
	}

	/** My Controller. */
	private Controller controller;
}

