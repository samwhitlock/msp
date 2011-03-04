package util;

/**This class is a wrapper class for point to put points into the tree structure
 * These may only reside below a LeafNode (parent). The purpose of this class
 * is so that any point may be removed or moved without having to traverse the entire 
 * tree to get to this point.
 * @author Sam Whitlock (cs61b-eo)
 * @param <Point>
 */
class Leaf<Point extends QuadTree.QuadPoint> {
	Point leaf;
	LeafNode<Point> parent;
	QuadTree<Point> tree;
	
	Leaf(Point p, LeafNode<Point> pt, QuadTree<Point> myTree ) {
		parent = pt;
		leaf = p;
		tree = myTree;
	}
	
	/**Asks the parent of THIS to remove THIS from its children
	 * @author Sam Whitlock (cs61b-eo)
	 */
	void remove() {
		parent.remove(this);
	}
	
	/**Asks the parent of THIS to move THIS to another LeafNode<Point>, if necessary
	 * @author Sam Whitlock (cs61b-eo)
	 */
	void move() {
		parent.move(this);
	}
	
	/**Returns the x coordinate of the internal Point
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	double x() {
		return leaf.x();
	}
	
	/**Returns the y coordinate of the internal Point
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	double y() {
		return leaf.y();
	}
}