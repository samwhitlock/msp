/* Do NOT modify the contents of this file.  It defines an API that we
 * will assume exists. */

package util;

import java.util.Iterator;

/** A set of moveable objects in a rectangular region of 2D space, 
 *  retrievable by position.  All objects in a Set2D reside within a 
 *  bounding rectangle, whose coordinates are retrievable.  
 *  It is legal to have two objects at the same location. */
public abstract class Set2D<Point extends Set2D.BasePoint> implements Iterable<Point> {

    /** A member of a Set2D. */
    public static class BasePoint {

        /** A new point at (X,Y). */
        public BasePoint (double x, double y) {
            this.x = x; this.y = y;
        }

        /** My current x coordinate. */
        public final double x () {
            return x;
        }

        /** My current y coordinate. */
        public final double y () {
            return y;
        }
        
        /** Move me to new position (X, Y). */
        public void move (double x, double y) {
            this.x = x; this.y = y;
        }

        private double x, y;

    }

    /** The current number of objects in the set. */
    public abstract int size ();

    /** Add P to THIS.  Causes an IllegalArgumentException if (X,Y) is not 
     *  in the bounding rectangle or P is already in THIS.
     *  Implementations of Set2D may also forbid Points belonging to
     *  multiple Set2Ds at once. */
    public abstract void add (Point p);

    /** Remove P from THIS, if it is currently a member, and
     *  otherwise do nothing. */
    public abstract void remove (Point p);

    /** Remove all points in THIS and place them in DEST. */
    public abstract void moveTo (Set2D<Point> dest);

    /** True iff THIS contains P. */
    public abstract boolean contains (Point p);

    /** The smallest x-coordinate allowed for objects in THIS.  All
     *  objects must have an x-coordinate that is >= llx (). */
    public abstract double llx ();

    /** The smallest y-coordinate allowed for objects in THIS.  All
     *  objects must have an y-coordinate that is >= lly (). */
    public abstract double lly ();

    /** The largest x-coordinate allowed for objects in THIS.  All
     *  objects must have an x-coordinate that is < urx (). */
    public abstract double urx ();

    /** The largest y-coordinate allowed for objects in THIS.  All
     *  objects must have an y-coordinate that is < ury (). */
    public abstract double ury ();

    /** An Iterator that returns all objects in THIS.  This Iterator 
     *  supports the remove operation.  It is an error to otherwise
     *  remove or insert additional Points into THIS during use of 
     *  the resulting Iterator, but existing Points may be moved. */
    public abstract Iterator<Point> iterator ();

    /** An Iterator that returns all objects in THIS whose coordinates
     *  lie on or within a rectangle whose lower-left coordinate is 
     *  (XL, YL) and whose upper-right coordinate is (XU, YU).  This 
     *  Iterator need not support the remove operation.  It is an error
     *  to otherwise remove or insert additional Points into THIS during 
     *  use of the resulting Iterator, or to move existing Points. */
    public abstract Iterator<Point> iterator (double xl, double yl, 
                                              double xu, double yu);

	/** True iff (x,y) is within the bounding box (llx,lly) to (urx, ury). */
	public static boolean isWithin (double x, double y,
                                    double llx, double lly, 
                                    double urx, double ury) {
		return x >= llx && x < urx && y >= lly && y < ury;
	}

    public static boolean isWithin (BasePoint p,
                                    double llx, double lly, 
                                    double urx, double ury) {
        return isWithin (p.x (), p.y (), llx, lly, urx, ury);
    }

}
  
