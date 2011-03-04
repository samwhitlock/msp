package reversigame;

import java.util.ArrayList;

import static java.lang.Integer.MIN_VALUE;

/** A Contestant that computes its own moves.
 * @author Sam Whitlock (cs61b-eo)
 * */
class AI extends Contestant {

	/** A new AI that will play MYCOLOR on BOARD, and use RAND as a
	 *  source for any necessary random numbers. */
	AI (Board board, PieceColor myColor) {
		super (board, myColor);
	}

	/** Assuming that the game is not over and it is my turn, make a
	 *  valid move on the board.  (Never tries to make an illegal move.)
	 *  @author Sam Whitlock (cs61b-eo)
	 *  */
	void takeMove () {
		if( !board.gameOver() && board.whoseMove() == myColor ) {
			int[] bestMove = bestMove(new Board(board));
			if( bestMove[0] == MIN_VALUE ) {
				if(!board.pass()) {
					board.resign();
				}
			} else board.makeMove(bestMove[0], bestMove[1]);
		}
	}

	/**This implements finding this best move based on a MIN-MAX tree
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Board
	 * @return int[]
	 */
	private int[] bestMove(Board board) {
		ArrayList<Board> results = new ArrayList<Board>();
		int depth = 4;
		bestMoveHelper(results, board, depth);
		if( results.isEmpty() ) {
			return new int[] { MIN_VALUE, MIN_VALUE };
		} else {
			Board bestBoard = null;
			for( Board bd : results ) {
				if( bestBoard == null || bd.boardValue(myColor) > bestBoard.boardValue(myColor) ) {
					bestBoard = bd;
				}
			}

			while( bestBoard.numMoves() > board.numMoves() + 1) {
				bestBoard.undo();
			}

			return bestBoard.parseMove(bestBoard.lastMove());
		}
	}

	/**A helper for the best move method that traverses down the min-max tree 
	 * to the given level
	 * @param ArrayList<Board>
	 * @param Board current board
	 * @param depth of min-max tree (int)
	 * @author Sam Whitlock (cs61b-eo)
	 */
	private void bestMoveHelper(ArrayList<Board> brdList, Board brd, int depth) {
		if( depth == 0 ) {
			brdList.add(brd);
		} else if( depth > 0 ) {
			if( brd.gameOver() ) {
				brdList.add(brd);
			} else {
				ArrayList<int[]> possibleMovesFromThisBoard = brd.legalMoves();
				for( int[] move : possibleMovesFromThisBoard ) {
					Board bufferBoard = new Board(brd);
					bufferBoard.makeMove(move[0], move[1]);
					bestMoveHelper(brdList, bufferBoard, depth - 1);
				}
			}
		}
	}
}

