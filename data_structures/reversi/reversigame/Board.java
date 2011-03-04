package reversigame;

import static java.lang.Integer.MIN_VALUE;
import static reversigame.PieceColor.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/** A class that represents a reversi board
 * @author Sam Whitlock (cs61b-eo)
 */
class Board {

	private PieceColor[][] board;
	private PieceColor currentPlayer;
	private ArrayList<Position> blackPieces, whitePieces;
	private int numberOfMoves;
	private static HashMap<String, Integer> letterToNumberMap = new HashMap<String, Integer>();
	static String[] numberToLetterMap;
	private String lastMove = null;
	private Board previousBoard = null;
	PieceColor resigned = EMPTY;

	static {
		letterToNumberMap.put("a", 0);
		letterToNumberMap.put("b", 1);
		letterToNumberMap.put("c", 2);
		letterToNumberMap.put("d", 3);
		letterToNumberMap.put("e", 4);
		letterToNumberMap.put("f", 5);
		letterToNumberMap.put("g", 6);
		letterToNumberMap.put("h", 7);
		numberToLetterMap = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
	}

	/**A constructor that copies the argument Board's parameters
	 * @author Sam Whitlock (cs61b-eo)
	 * @param a Board whose instance vars will be copied to THIS's instance vars
	 */
	Board (Board brd) {
		set(brd);
	}

	/**Compares board based on their current state
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Board to compare
	 * @return true iff compared board is equal
	 */
	boolean equalsBoard(Board bd) {
		for( int x = 0; x < 8; x++ ) {
			for( int y = 0; y < 8; y++ ) {
				if( bd.board[x][y] != board[x][y]) {
					return false;
				}
			}
		}

		return bd.numberOfMoves == numberOfMoves &&
		bd.currentPlayer == currentPlayer &&
		bd.lastMove.compareTo(lastMove) == 0;
	}

	/** A new, cleared board at the start of the game.
	 * @author Sam Whitlock (cs61b-eo)
	 * */
	Board () {
		board = new PieceColor[8][8];
		setBlank();
		board[3][3] = WHITE;
		board[4][4] = WHITE;
		board[3][4] = BLACK;
		board[4][3] = BLACK;
		currentPlayer = BLACK;
		blackPieces = new ArrayList<Position>();
		blackPieces.add(new Position(3, 4)); blackPieces.add(new Position(4, 3));
		whitePieces = new ArrayList<Position>();
		whitePieces.add(new Position(3, 3)); whitePieces.add(new Position(4, 4));
		numberOfMoves = 0;
	}

	/**A utility that sets all the values in board to EMPTY
	 * Assumes that board is not null
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 */
	private void setBlank() {
		for( int i = 0; i < 8; i++ ) {
			Arrays.fill(board[i], EMPTY);
		}
	}

	/** Clear me to my starting state, with pieces in their initial
	 *  positions.
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @param void
	 *  @return void
	 *  */
	void clear () {
		board = new PieceColor[8][8];
		setBlank();
		board[3][3] = WHITE;
		board[4][4] = WHITE;
		board[3][4] = BLACK;
		board[4][3] = BLACK;
		currentPlayer = BLACK;
		numberOfMoves = 0;
		previousBoard = null;
		resigned = EMPTY;
		lastMove = null;
		blackPieces.clear();
		blackPieces.add(new Position(3, 4)); blackPieces.add(new Position(4, 3));
		whitePieces.clear();
		whitePieces.add(new Position(3, 3)); whitePieces.add(new Position(4, 4));
	}

	/** True iff the current board allows no further moves by either side.
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return true if game is over
	 * */
	boolean gameOver () {
		if( numberOfMoves == 60 ) {
			return true;
		} else {
			ArrayList<int[]> currentMoves = legalMoves();
			currentPlayer = currentPlayer.opposite();
			ArrayList<int[]> otherMoves = legalMoves();
			currentPlayer = currentPlayer.opposite();
			if( currentMoves.isEmpty() && otherMoves.isEmpty() ) {
				return true;
			} else return false;
		}
	}

	/** Number of black pieces on the board.]
	 * @author Sam Whitlock (cs61b-eo)
	 * @return int
	 * @param void
	 * */
	int blackPieces () {
		return blackPieces.size();
	}

	/** Number of white pieces on the board.
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return int
	 * */
	int whitePieces () {
		return whitePieces.size();
	}

	/** The current contents of square SQ, which is of the form "CR",
	 *  where 'a' <= C <= 'h', and  '1' <= R <= '8'.
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @param String representative of position
	 *  @return PieceColor at position
	 *  */
	PieceColor get (String sq) {
		int[] move = parseMove(sq);
		return get(move[0], move[1]);
	}

	/** The current contents of square (C, R), where C and R are in
	 *  the range 0..7.  C denotes the column (0 => column 'a', 1 => column 
	 *  'b', etc.), and R denotes the row (0 => row 1, etc.).
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @param int column num
	 *  @param int row num
	 *  @return PieceColor at position
	 *  */
	PieceColor get (int c, int r) {
		return board[c][r];
	}

	/**Convert a move from its string representation to its int[2] representation
	 * @author Sam Whitlock (cs61b-eo)
	 * @param String move
	 * @return int[] representative of move
	 */
	int[] parseMove(String mv) {
		return new int[] {letterToNumberMap.get(mv.substring(0, 1).toLowerCase()), Integer.parseInt(mv.substring(1, 2)) - 1};
	}

	/** True iff SQ is a legal move in the current position. SQ has the
	 *  same format as for {@link #get(java.lang.String) get}.
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @return boolean true if move was legal
	 *  @param String representative of move
	 *  */
	boolean legalMove (String sq) {
		int[] move = parseMove(sq);
		return legalMove(move[0], move[1]);
	}

	/** True iff is legal to move to (C, R) in the current position. (C, R) 
	 *  has the same meaning as for {@link #get(int,int) get}.
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @return boolean if it was a legal move
	 *  @param int column num
	 *  @param int row num
	 *  */
	boolean legalMove (int c, int r) {
		if( board[c][r] == EMPTY ) {
			return processBoard(null, c, r);
		} else return false;
	}

	/**Returns all the available legal moves for the current player
	 * @author Sam Whitlock (cs61b-eo)
	 * @return ArrayList<int[]>
	 */
	ArrayList<int[]> legalMoves() {
		ArrayList<int[]> retList = new ArrayList<int[]>();
		for(int i = 0; i < 8; i++) {
			for( int j = 0; j < 8; j++) {
				if(legalMove(i, j)) {
					retList.add(new int[] {i, j});
				}
			}
		}
		return retList;
	}

	/** Return the color of the player who has the next move.  The
	 *  value is arbitrary if {@link #gameOver}.
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @param void
	 *  @return PieceColor (current player)
	 *  */
	PieceColor whoseMove () {
		return currentPlayer;
	}

	/**Determine if a move is legal and execute it if floppedPositions isn't null
	 * @author Sam Whitlock (cs61b-eo)
	 * @param floppedPositions
	 * @param xPos
	 * @param yPos
	 * @return boolean if the move is legal
	 */
	private boolean processBoard(ArrayList<Position> floppedPositions, int xPos, int yPos) {
		if(floppedPositions == null) {
			return
			processMove(null, xPos, yPos, 0, 1) ||
			processMove(null, xPos, yPos, 0, -1) || 
			processMove(null, xPos, yPos, 1, 0) ||
			processMove(null, xPos, yPos, -1, 0) ||
			processMove(null, xPos, yPos, 1, 1) ||
			processMove(null, xPos, yPos, -1, -1) ||
			processMove(null, xPos, yPos, 1, -1) ||
			processMove(null, xPos, yPos, -1, 1);
		} else {
			processMove(floppedPositions, xPos, yPos, 0, 1);
			processMove(floppedPositions, xPos, yPos, 0, -1);
			processMove(floppedPositions, xPos, yPos, 1, 0);
			processMove(floppedPositions, xPos, yPos, -1, 0);
			processMove(floppedPositions, xPos, yPos, 1, 1);
			processMove(floppedPositions, xPos, yPos, -1, -1);
			processMove(floppedPositions, xPos, yPos, 1, -1);
			processMove(floppedPositions, xPos, yPos, -1, 1);
			if( floppedPositions.isEmpty() ) {
				return false;
			} else {
				floppedPositions.add(new Position(xPos, yPos));
				return true;
			}
		}
	}

	/**Return the bounds opponents for one vector for a given move
	 * @author Sam Whitock (cs61b-eo)
	 * @param floppedPositions (ArrayList)
	 * @param xPos
	 * @param yPos
	 * @param xVel
	 * @param yVel
	 * @return boolean if vector is valid
	 */
	private boolean processMove(ArrayList<Position> floppedPositions, int xPos, int yPos, int xVel, int yVel) {
		boolean boundOpponents = false, surroundable = false, addFlops = floppedPositions != null;
		ArrayList<Position> addedFlops = new ArrayList<Position>();

		for( int x = xPos + xVel, y = yPos + yVel; x >= 0 && y >= 0 && x < 8 && y < 8; x += xVel, y += yVel ) {
			if( board[x][y] == EMPTY ) {
				return false;
			} else if( board[x][y] == currentPlayer ) {
				surroundable = true;
				break;
			} else if( board[x][y] == currentPlayer.opposite() ) {
				boundOpponents = true;
				if( addFlops ) {
					addedFlops.add(new Position(x, y));
				}
			}
		}

		if( boundOpponents && surroundable ) {
			if( addFlops ) {
				floppedPositions.addAll(addedFlops);
			}
			return true;
		} else return false;
	}

	/** Total number of calls to makeMove and pass since the last
	 *  clear or creation of the board.
	 *  @author Sam Whitlock (cs61b-eo)
	 *  */
	int numMoves () {
		return numberOfMoves;
	}

	/** Perform the move SQ.  Returns true if SQ was performed as a move 
	 * (i.e. if it was legal or not)
	 * @author Sam Whitock (cs61b-eo)
	 * @return boolean
	 * @param String representing move
	 * */
	boolean makeMove (String sq) {
		int[] move = parseMove(sq);
		return makeMove(move[0], move[1]);
	}

	/** Perform the move CR, where legalMove (C,R), returning 
	 * true if it is a valid move.
	 * @author Sam Whitlock (cs61b-eo)
	 * @param int column num
	 * @param int row num
	 * @return boolean
	 * */
	boolean makeMove (int c, int r) {
		if( legalMove(c, r) && !resigned()) {
			Board newPrevBoard = new Board(this);
			previousBoard = newPrevBoard;
			ArrayList<Position> floppedPositions = new ArrayList<Position>();
			processBoard(floppedPositions, c, r);
			for( Position pos : floppedPositions ) {
				board[pos.position[0]][pos.position[1]] = currentPlayer;
			}

			if( currentPlayer == BLACK ) {
				whitePieces.removeAll(floppedPositions);
				blackPieces.addAll(floppedPositions);
			} else {
				blackPieces.removeAll(floppedPositions);
				whitePieces.addAll(floppedPositions);
			}

			currentPlayer = currentPlayer.opposite();
			numberOfMoves++;
			lastMove = numberToLetterMap[c] + (1 + r);
			return true;
		} else return false;
	}

	/** The last move taken, or null if numMoves()==0.  Either a "-"
	 *  (for pass) or a two-character String in the standard move
	 *  notation.
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @param void
	 *  @return String representing the last move
	 *  */
	String lastMove () {
		return lastMove;
	}

	/** Update to indicate that the current player passes, assuming it
	 *  is legal to do so.  The only effect is to change whoseMove ().
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @param void
	 *  @return void
	 *  */
	boolean pass () {
		if( resigned() ) {
			return false;
		} else {
			ArrayList<int[]> legalMoves = legalMoves();
			if( !gameOver() && legalMoves.isEmpty() ) {
				Board newPrevBoard = new Board(this);
				previousBoard = newPrevBoard;
				currentPlayer = currentPlayer.opposite();
				lastMove = "-";
				return true;
			} else return false;
		}
	}

	/** End the game, indicating that the current player resigned.
	 * @author Sam Whitlock (cs61b-eo)
	 * @param void
	 * @return void
	 * */
	void resign () {
		resigned = currentPlayer;
		currentPlayer = currentPlayer.opposite();
	}

	/**Resign the board for the given player
	 * @author Sam Whitlock (cs61b-eo)
	 * @return void
	 * @param PieceColor : player to resign from the board
	 */
	void resign(PieceColor player) {
		if( player != EMPTY ) {
			resigned = player;
			currentPlayer = player.opposite();
		}
	}

	/** Indicates that the losing player resigned. Remains set until 
	 *  next clear().
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @return boolean
	 *  @param void
	 *  */
	boolean resigned () {
		return resigned != EMPTY;
	}

	/** Set my current state to be a copy of that of BOARD.
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Board
	 * @return void
	 * */
	void set (Board board) {
		this.board = new PieceColor[8][8];
		setBlank();
		for( int i = 0; i < 8; i++) {
			System.arraycopy(board.board[i], 0, this.board[i], 0, 8);
		}
		this.currentPlayer = board.currentPlayer;
		this.blackPieces = new ArrayList<Position>(board.blackPieces);
		this.whitePieces = new ArrayList<Position>(board.whitePieces);
		this.numberOfMoves = board.numberOfMoves;
		if( board.lastMove != null )
			this.lastMove = new String(board.lastMove);
		this.previousBoard = board.previousBoard;
		this.resigned = board.resigned;
	}

	/** Restore state of the board to that preceding the last makeMove or
	 *  pass operation.  As a result, also restores the values of numMoves(),
	 *  gameOver(), and lastMove().  Requires that numMoves () > 0 
	 *  and !resigned ().
	 *  @author Sam Whitlock (cs61b-eo)
	 *  @param void
	 *  @return void
	 *  */
	void undo () {
		if( previousBoard != null && !resigned() )
			set (previousBoard);
	}

	/**Return the value of the board based on the difference in the 
	 * number of pieces for the current board
	 * @param PieceColor : player for whom to determine the value of this board state
	 * @return integer boad value
	 * @author Sam Whitlock (cs61b-eo)
	 */
	int boardValue(PieceColor player) {
		if( player == BLACK ) {
			return blackPieces() - whitePieces();
		} else if( player == WHITE ) {
			return whitePieces() - blackPieces();
		} else return MIN_VALUE;
	}
}

