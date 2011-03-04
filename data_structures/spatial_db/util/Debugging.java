package util;

/** A utility class for handling debugging.
 * @author Sam Whitlock (cs61b-eo)
 * */
public class Debugging {

    private static int debuggingLevel = 0;

    /** The current level of debugging information.  0 == no
     *  debugging (the initial setting). */
    public static void setDebuggingLevel (int level) {
        debuggingLevel = level;
    }

    /** If LEVEL is positive and less than the current debuggingLevel, 
     *  print FORMAT and ARGS, as for printf.  Otherwise, do nothing. */
    public static void Debug (int level, String format, Object... args) {
        if (level > 0 && debuggingLevel >= level)
            System.err.printf (format, args);
    }
    
    /**Prints a warning if 2 points are overlapping if debugging option is 1 or greater
     * @author Sam Whitlock (cs61b-eo)
     * @param <Point>
     * @param p1
     * @param p2
     */
    static <Point extends QuadTree.QuadPoint> void pointCollision( Point p1, Point p2 ) {
    	if( debuggingLevel > 0) {
    		System.err.println("error : 2 points overlap and/or intersect");
    	}
    }
    
    /**Prints a warning if a point is attempted to be added not in a given bounds if the debugging option is greater than 0
     * @author Sam Whitlock (cs61b-eo)
     * @param x
     * @param y
     * @param bounds
     */
    static void pointNotInBounds( double x, double y, double[] bounds ) {
    	if( debuggingLevel > 0) {
    		System.err.println("error : point not in bounds");
    	}
    }
}

