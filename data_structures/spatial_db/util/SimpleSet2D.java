/** A simple Set2D implementation.  This is too slow for large sets of
 *  data. */

/* Author: P. N. Hilfinger */

package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static java.lang.Double.*;

/** A Set2D implemented as a simple list of Points.  The type argument, Point,
 *  indicates the type of points contained in the set.  As for QuadTree,
 *  the type parameter structure here allows you to extend
 *  SimplePoint, and thus add additional data and methods to the points
 *  you store. */
public class SimpleSet2D<Point extends Set2D.BasePoint> extends Set2D<Point> {

    /** An empty set whose bounding rectangle is infinite. */
    public SimpleSet2D () {
        points = new ArrayList<Point> ();
    }

    /* PUBLIC METHODS.  See Set2D.java for documentation */

    public int size () {
        return points.size ();
    }

    public void add (Point p) {
        if (points.contains (p))
            throw new IllegalArgumentException ("point added twice");
        points.add (p);
    }

    public void remove (Point p) {
        points.remove (p);
    }

    public void moveTo (Set2D<Point> dest) {
        if (dest == this)
            return;
        for (Point p : points) 
            dest.add (p);
        points.clear ();
    }

    public boolean contains (Point p) {
        return points.contains (p);
    }

    public double llx () {
        return NEGATIVE_INFINITY;
    }

    public double lly () {
        return NEGATIVE_INFINITY;
    }

    public double urx ()  {
        return POSITIVE_INFINITY;
    }

    public double ury ()  {
        return POSITIVE_INFINITY;
    }

    public Iterator<Point> iterator () {
        return points.iterator ();
    }

    public Iterator<Point> iterator (double xl, double yl, 
                                     double xu, double yu) {
        return new BoundedIterator (xl, yl, xu, yu);
    }

    /* END OF PUBLIC MEMBERS */

    private ArrayList<Point> points;

    private class BoundedIterator implements Iterator<Point> {
        /** An iterator over all points in SimpleSet2D.this that fall
         *  within the bounding box (XL,YL) .. (XU, YU). */
        BoundedIterator (double xl, double yl, 
                         double xu, double yu) {
            this.xl = xl; this.yl = yl;
            this.xu = xu; this.yu = yu;
            p = points.iterator ();
            advance ();
        }

        public boolean hasNext () {
            return next != null;
        }

        public Point next () {
            Point result = next;
            if (next == null)
                throw new NoSuchElementException ();
            advance ();
            return result;
        }

        public void remove () {
            p.remove ();
        }

        /** Set next to next point in p that is in bounds. */
        private void advance () {
            while (p.hasNext ()) {
                next = p.next ();
                if (isWithin (next, xl, yl, xu, yu)) 
                    return;
            }
            next = null;
        }

        private Iterator<Point> p;
        private Point next;
        double xl, yl, xu, yu;
    }

}


