package reversigame;

/**A class that wraps a 2D int array to represent a position on the board
 * This was necessary for the equals method in several Arraylists of positions 
 * through the program
 * @author Sam Whitock (cs61b-eo)
 */
public class Position {
	int[] position;

	Position( int c, int r ) {
		position = new int[] {c, r};
	}

	/**Returns true if obj is an instance of Position and the int[] that they both wrap 
	 * have equal values
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Object obj
	 * @return boolean
	 */
	public boolean equals(Object obj) {
		if( obj == null ) {
			return false;
		} else {
			if( this == obj ) {
				return true;
			} else if ( getClass() == obj.getClass() ) {
				Position pos = (Position) obj;
				return position[0] == pos.position[0] &&
				position[1] == pos.position[1];
			} else return false;
		}
	}
}