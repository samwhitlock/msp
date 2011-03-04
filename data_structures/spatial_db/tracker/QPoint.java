package tracker;

import util.QuadTree.QuadPoint;

/**A custom class to represents points in a QuadTree
 * Note: this class has a natural ordering that is inconsistent with equals.
 * Equals compares points based on their positions and velocities, while the
 * compareTo method compares objects based on the natural ordering of their
 * integer IDs.
 * @author Sam Whitlock (cs61b-eo)
 */
class QPoint extends QuadPoint implements Comparable<QPoint> {

	double xVelocity, yVelocity;
	private int ID;

	QPoint( int ident, double x, double y, double xVel, double yVel) {
		super(x, y);
		xVelocity = xVel;
		yVelocity = yVel;
		ID = ident;
	}

	/** Compares 2 QPoint objects and returns true iff
	 * they have equal positions and velocities
	 * @author Sam Whitlock (cs61b-eo)
	 * @param QPoint object to compare to
	 * @return true iff the objects have the same positions and velocities
	 */
	@Override
	public boolean equals(Object obj) {
		if( obj == null ) {
			return false;
		} else {
			if( this == obj ) {
				return true;
			} else if ( getClass() == obj.getClass() ) {
				QPoint pt = (QPoint) obj;
				return
				x() == pt.x() &&
				y() == pt.y() &&
				xVelocity == pt.xVelocity() &&
				yVelocity == pt.yVelocity();
			} else return false;
		}
	}

	/**Returns the integer ID of this QPoint
	 * @author Sam Whitlock (cs61b-eo)
	 * @return the integer ID
	 */
	public int id() {
		return ID;
	}

	/**Returns the x velocity of THIS
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	public double xVelocity() {
		return xVelocity;
	}

	/**Returns the y velocity of THIS
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	public double yVelocity() {
		return yVelocity;
	}

	/**Compares this QPoint to the parameter and compares them based
	 * on the natural ordering of their integer IDs. Note that this is 
	 * inconsistent with the equals method, which compares points based on their positions
	 * and velocities.
	 * @author Sam Whitlock (cs61b-eo)
	 * @param QPoint to be compared to this
	 * @returns -1, 0, or 1 if the ID of this is less than, equal to, or greater than the ID of the QPoint parameter 
	 */
	@Override
	public int compareTo(QPoint pt) {
		if( pt.id() == ID ) {
			return 0;
		} else if( pt.id() < ID ) {
			return 1;
		} else return -1;
	}
}
