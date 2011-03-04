package util;

import java.util.ArrayList;

/** Represents a single node in a quad tree.  This is abstract in case
 *  you want to have different kinds of QuadTreeNode corresponding to
 *  empty trees, leaf trees (no children, just objects), and internal
 *  trees (those with children).
 *  @author Sam Whitlock (cs61b-eo)
 *  */
class QuadTreeNode<Point extends QuadTree.QuadPoint> {
	QuadTreeNode<Point> parent;
	QuadTree<Point> myTree;

	protected double[] center, bounds;
	protected final int max_children = 10;/**this number may be changed to alter performance*/

	/**An ArrayList that represents my children.
	 * children[0] = NW Quadrant (quadrant 1)
	 * children[1] = NE Quadrant (quadrant 2)
	 * children [2] = SE Quadrant (quadrant 3)
	 * children [3] = SW Quadrant (quadrant 4)
	 */
	ArrayList<QuadTreeNode<Point>> children = new ArrayList<QuadTreeNode<Point>>();
	QuadTreeNode<Point> NW = null, NE = null, SE = null, SW = null;

	/** A new QuadTreeNode that is part of WHOLETREE. */ 
	QuadTreeNode ( QuadTree<Point> tree, QuadTreeNode<Point> parent, double llX, double llY, double urX, double urY) {
		this.parent = parent;
		myTree = tree;
		bounds = new double[] { llX, llY, urX, urY };
		center = new double[] { (urX + llX) / 2, (urY + llY) / 2 };
		children.add(NE); children.add(NW); children.add(SW); children.add(SE);
	}

	/**return the lower left x bounds
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	double llx () {
		return bounds[0];
	}

	/**return the lower left y bounds
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	double lly () {
		return bounds[1];
	}

	/**return the upper right x bounds
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	double urx () {
		return bounds[2];
	}

	/**return the upper right y bounds
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	double ury () {
		return bounds[3];
	}

	/**return the center x coordinate
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	double x() {
		return center[0];
	}

	/**return the center y coordinate
	 * @author Sam Whitlock (cs61b-eo)
	 * @return double
	 */
	double y() {
		return center[1];
	}

	/**Make a leaf node with the coordinates of the appropriate quadrant
	 * @author Sam Whitlock (cs61b-eo)
	 * @param int quadrant
	 * @return LeafNode<Point>
	 */
	private LeafNode<Point> makeLeafNode(int quadrant) {
		LeafNode<Point> returnNode = null;
		if ( quadrant == 0 ) {
			returnNode = new LeafNode<Point>(myTree, this, x(), y(), urx(), ury());
		} else if( quadrant == 1 ) {
			returnNode = new LeafNode<Point>(myTree, this, llx(), y(), x(), ury());
		} else if( quadrant == 2 ) {
			returnNode = new LeafNode<Point>(myTree, this, llx(), lly(), x(), y());
		} else if( quadrant == 3 ) {
			returnNode = new LeafNode<Point>(myTree, this, llx(), lly(), x(), y());
		}
		return returnNode;
	}

	/**Determine the quadrant a point is in based on its location
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Point
	 * @return integer quadrant
	 */
	private int determineQuadrant(Point pt) {
		int quadrant;
		if (pt.x() >= x() ) {
			if (pt.y() >= y() ) {
				quadrant = 0;
			} else {
				quadrant = 3;
			}
		} else {
			if (pt.y() >= y() ) {
				quadrant = 1;
			} else {
				quadrant = 2;
			}
		}
		return quadrant;
	}

	/**Replaces a node that is a child of this with another node
	 * @author Sam Whitlock (cs61b-eo)
	 * @param QuadTreeNode oldNode
	 * @param QuadTreeNode newNode
	 */
	protected void replace(QuadTreeNode<Point> pt, QuadTreeNode<Point> newNode) {
		if( children.contains(pt) ) {
			children.set(children.indexOf(pt), newNode);
			if( isGarbageNode() ) {
				if( parent == null ) {
					myTree.setRoot(new LeafNode<Point>(myTree, null, llx(), lly(), urx(), ury()));
				} else {
					parent.replace(this, null);
				}
			} else {
				ArrayList<Leaf<Point>> childs = boundedChildren( llx(), lly(), urx(), ury() );
				if( childs.size() <= max_children ) {
					LeafNode<Point> newNode1 = new LeafNode<Point>( myTree, parent, llx(), lly(), urx(), ury() );
					for( Leaf<Point> leafy : childs ) {
						newNode1.insert(leafy);
					}
					if( parent == null ) {
						myTree.setRoot(newNode1);
					} else {
						parent.replace(this, newNode1);
					}
				}
			}
		}
	}

	/**Returns true if this node has no children
	 * @author Sam Whitlock (cs61b-eo)
	 * @return boolean
	 */
	private boolean isGarbageNode() {
		boolean ret = true;
		for( QuadTreeNode<Point> x : children ) {
			if ( x != null ) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	/**Insert a Leaf into THIS
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Leaf<Point>
	 * @return THIS
	 */
	QuadTreeNode<Point> insert(Leaf<Point> lf) {
		int quadrant = determineQuadrant(lf.leaf);
		if(children.get(quadrant) == null) {
			children.set(quadrant, makeLeafNode(quadrant));
		}
		children.set(quadrant, children.get(quadrant).insert(lf));
		return this;
	}

	/**Returns an arrayList of children in this node (if any) bounded by the given bounds
	 * @author Sam Whitlock (cs61b-eo)
	 * @param llx
	 * @param lly
	 * @param urx
	 * @param ury
	 * @return an ArrayList<Leaf<Point>> of the bounded children
	 */
	ArrayList<Leaf<Point>> boundedChildren( double llx, double lly, double urx, double ury ) {
		ArrayList<Leaf<Point>> retList = new ArrayList<Leaf<Point>>();
		for( QuadTreeNode<Point> node : children ) {
			if( node != null && node.intersects(llx, lly, urx, ury) ) {
				retList.addAll(node.boundedChildren(llx, lly, urx, ury));
			}
		}
		return retList;
	}
	
	/**Moves a leaf within this (if it exists within this)
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Leaf<Point>
	 */
	void move(Leaf<Point> lf) {
		if( Set2D.isWithin(lf.leaf, llx(), lly(), urx(), ury()) ) {
			int quadrant = determineQuadrant(lf.leaf);
			if( children.get(quadrant) == null ) {
				children.set(quadrant, makeLeafNode(quadrant));
			}
			children.set(quadrant, children.get(quadrant).insert(lf));
		} else if( parent != null ) {
			parent.move(lf);
			if( isGarbageNode() ) {
				parent.replace(this, null);
			}
		}
	}

	/** Returns true if the given bounds overlap with the bounds of this node
	 * @author Sam Whitlock (cs61b-eo)
	 * @param llx
	 * @param lly
	 * @param urx
	 * @param ury
	 * @return boolean
	 */
	private boolean intersects(double llx, double lly, double urx, double ury) {
		return urx >= llx() || ury >= lly() || llx <= urx() || lly <= ury();  
	}
}