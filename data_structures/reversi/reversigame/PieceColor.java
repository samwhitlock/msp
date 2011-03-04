package reversigame;

/** Describes the occupants of a square on a Reversi board.
 * @author Sam Whitlock (cs61b-eo)
 * */
enum PieceColor {

	/** EMPTY: no piece.
	 *  BLACK, WHITE: pieces. */
	EMPTY,
	BLACK { 
		PieceColor opposite () { 
			return WHITE;
		}

		String pieceChar() {
			return "b";
		}

		String asString() {
			return "black";
		}
	}, 
	WHITE {
		PieceColor opposite () { 
			return BLACK;
		}

		String pieceChar() {
			return "w";
		}

		String asString() {
			return "white";
		}
	};

	/** The piece color of my opponent, if defined. */
	PieceColor opposite () {
		throw new UnsupportedOperationException ();
	}

	/** The piece color of 
	 * @author Sam Whitlock (cs61b-eo)
	 * @return String
	 */
	String pieceChar() {
		return "-";
	}

	/** The my color as a string
	 * @author Sam Whitlock (cs61b-eo)
	 * @return String
	 */
	String asString() {
		return "";
	}
}
