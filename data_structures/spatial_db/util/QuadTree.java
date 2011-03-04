package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

/** A Set2D implemented with a QuadTree.  The type argument, Point,
 *  indicates the type of points contained in the set.  The rather
 *  involved type parameter structure here allows you to extend 
 *  QuadPoint, and thus add additional data and methods to the points
 *  you store.
 *  @author Sam Whitlock (cs61b-eo)
 *  */
public class QuadTree<Point extends QuadTree.QuadPoint> extends Set2D<Point> {

	/** The supertype of all possible kinds of QuadTree members.
	 *  Type arguments to QuadTree are subtypes of QuadPoint. */
	public static class QuadPoint extends Set2D.BasePoint {
		@SuppressWarnings("unchecked")
		QuadTree myTree;

		public QuadPoint (double x, double y) {
			super (x, y);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void move(double x, double y) {
			super.move(x, y);
			((Leaf) myTree.leaves.get(this)).move();
		}
	}

	/** An empty set whose bounding rectangle has lower-left coordinates
	 *  (LLX,LLY) and upper-right coordinates (URX,URY).  The argument
	 *  DELTA has no externally visible effect, but may affect performance.
	 *  It is intended to specify the smallest possible dimension of 
	 *  any subtree's bounding rectangle.  No rectangle with a side smaller
	 *  than DELTA is subdivided.  */
	public QuadTree (double llx, double lly, double urx, double ury,
			double del) {
		lowerLeftX = llx;
		lowerLeftY = lly;
		upperRightX = urx;
		upperRightY = ury;
		delta = .001;
		radius = del / 2;
		leaves = new HashMap<Point, Leaf<Point>>();
		root = new LeafNode<Point>( this, null, llx, lly, urx, ury);
	}

	/** The root node, which contains (along with its subtrees) all points
	 *  in THIS tree. */
	private QuadTreeNode<Point> root;
	private double lowerLeftX, lowerLeftY, upperRightX, upperRightY;
	double radius, delta;
	HashMap<Point, Leaf<Point>> leaves;

	/** Returns the number of children within this tree
	 * @return int
	 * @author Sam Whitlock (cs61b-eo)
	 */
	public int size () {
		return leaves.size();
	}

	public void add (Point p) {
		if( Set2D.isWithin(p, lowerLeftX + radius, lowerLeftY + radius, upperRightX - radius, upperRightY - radius) && !leaves.containsKey(p) ) {
			Iterator<Point> isOK = iterator( p.x() - radius, p.y() - radius, p.x() + radius, p.y() + radius );

			while( isOK.hasNext() ) {
				Point pt = isOK.next();
				if(radiusCheck(p, pt, delta )) {
					Debugging.pointCollision(pt, p);
					return;
				}
			}

			Leaf<Point> leaf = new Leaf<Point>( p, null, this );
			p.myTree = this;
			leaves.put(p, leaf);
			root = root.insert( leaf );
		}
	}

	public void remove (Point p) {
		if( leaves.containsKey(p) ) {
			leaves.get(p).remove();
			leaves.remove(p);
		}
	}

	/**Sets the root of this tree
	 * @author Sam Whitlock (cs61b-eo)
	 * @param QuadTreeNode<Point>
	 */
	void setRoot(QuadTreeNode<Point> root) {
		this.root = root;
	}

	public void moveTo (Set2D<Point> dest) {
		Iterator<Entry<Point, Leaf<Point>>> iter = leaves.entrySet().iterator();
		while( iter.hasNext() ) {
			Entry<Point, Leaf<Point>> pt = iter.next();
			dest.add(pt.getKey());
			remove(pt.getKey());			
			iter.remove();
		}

		leaves.clear();
	}

	public boolean contains (Point p) {
		return leaves.containsKey(p); 
	}

	public double llx () {
		return lowerLeftX;
	}

	public double lly () {
		return lowerLeftY;
	}

	public double urx ()  {
		return upperRightX;
	}

	public double ury ()  {
		return upperRightY;
	}

	/**Checks to see if 2 objects are at least distance apart, and return true if so
	 * @author Sam Whitlock (cs61b-eo)
	 * @param <Point>
	 * @param pt1
	 * @param pt2
	 * @param dist
	 * @return boolean
	 */
	private static <Point extends QuadTree.QuadPoint> boolean radiusCheck( Point pt1, Point pt2, double dist ) {
		return radiusCheck( pt1.x(), pt1.y(), pt2.x(), pt2.y(), dist );
	}

	/**Checks to see if 2 objects are at least distance apart, and return true if so
	 * @author Sam Whitlock (cs61b-eo)
	 * @param <Point>
	 * @param pt1
	 * @param pt2
	 * @param dist
	 * @return boolean
	 */
	private static boolean radiusCheck( double x1, double y1, double x2, double y2, double dist) {
		return dist >= Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

	public Iterator<Point> iterator () {
		ArrayList<Point> newp = new ArrayList<Point>(leaves.keySet());
		return new Iter(newp.iterator(), this);
	}

	public Iterator<Point> iterator (double lowerLeftX, double lowerLeftY, 
			double upperRightX, double upperRightY) {
		return new BoundedIterator( lowerLeftX, lowerLeftY, upperRightX, upperRightY, this );
	}

	/**My implementation of an iterator over all of the points. It does support the remove operation
	 * and just iterates over the points in the HashMap in THIS 
	 * @author Sam Whitlock (cs61b-eo)
	 */
	private class Iter implements Iterator<Point> {

		private Iterator<Point> internalIterator;
		private Point item;
		private QuadTree<Point> tree;

		Iter(Iterator<Point> iter, QuadTree<Point> myTree) {
			internalIterator = iter;
			tree = myTree;
		}

		@Override
		public boolean hasNext() {
			return internalIterator.hasNext();
		}

		@Override
		public Point next() throws NoSuchElementException {
			item = internalIterator.next();
			return item;
		}

		@Override
		public void remove() throws UnsupportedOperationException {
			if( item != null ) {
				tree.leaves.get(item).remove();
				internalIterator.remove();
				tree.leaves.remove(item);
				item = null;
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}

	/**My implementation of a bounded iterator over the tree
	 * This is represented internally by an iterator of an arraylist that 
	 * THIS gets by calling the method boundedChildren on the root of the tree
	 * @author Sam Whitlock (cs61b-eo)
	 */
	private class BoundedIterator implements Iterator<Point> {

		private double lowerLeftX, lowerLeftY, upperRightX, upperRightY;
		private QuadTree<Point> tree;
		private Point item;
		private Iterator<Leaf<Point>> internalIterator;

		BoundedIterator( double llX, double llY, double urX, double urY, QuadTree<Point> myTree ) {
			lowerLeftX = llX; lowerLeftY = llY; upperRightX = urX; upperRightY = urY; tree = myTree;
			internalIterator = tree.root.boundedChildren(lowerLeftX, lowerLeftY, upperRightX, upperRightY).iterator();
		}

		@Override
		public boolean hasNext() {
			return internalIterator.hasNext();
		}

		@Override
		public Point next() throws NoSuchElementException {
			item = internalIterator.next().leaf;
			return item;
		}

		@Override
		public void remove() throws UnsupportedOperationException {
			if( item != null ) {
				tree.leaves.get(item).remove();
				tree.leaves.remove(item);
				item = null;
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}
}