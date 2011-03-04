package tracker;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** Central dispatching point for all testing.   */
public class Testing {

    /** Run the JUnit tests in the tracker package. */
    public static void main (String[] ignored) {
        textui.runClasses (tracker.Testing.class);
    }

    @Test public void dummy () {
    }
    
    /** Testing the public members of QPoint
     * @author Sam Whitlock (cs61b-eo)
     * */
    @Test public void QPoint1 () {
    	QPoint myPoint = new QPoint(52, 15, 29, -11, 71);
    	assertTrue(myPoint.id() == 52);
    	assertTrue(myPoint.x() == 15 );
    	assertTrue(myPoint.y() == 29 );
    	assertTrue(myPoint.xVelocity == -11 );
    	assertTrue(myPoint.yVelocity == 71 );
    }
    
    /**testing the equals method
     * @author Sam Whitlock (cs61b-eo)
     */
    @Test public void QPoint2() {
    	QPoint myPoint = new QPoint( 52, 1, 2, 3, 4 );
    	QPoint myPt = new QPoint(51, 1, 2, 3, 4);
    	assertTrue( myPoint.equals(myPt) );
    }
    
    /**Testing the override of compareTo
     * @author Sam Whitlock (cs61b-eo)
     */
    @Test public void QPoint3() {
    	QPoint myPoint = new QPoint( 99, 1, 2, 3, 4);
    	QPoint myPt = new QPoint( 98, 1, 2, 3, 4 );
    	QPoint myPt1 = new QPoint( 100, 1, 2, 3, 4 );
    	QPoint myPt2 = new QPoint( 99, 1, 2, 3, 4);
    	assertTrue( myPoint.compareTo(myPt2) == 0 );
    	assertTrue( myPoint.compareTo(myPt1) == -1);
    	assertTrue( myPoint.compareTo(myPt) == 1);
    }
}