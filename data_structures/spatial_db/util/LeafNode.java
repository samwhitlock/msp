package util;

import java.util.ArrayList;

/**My representation of a leafNode. This node may resides at most 2 levels above the bottom
 * level of the tree
 * @author Sam Whitlock (cs61b-eo)
 * @param <Point>
 */
class LeafNode<Point extends QuadTree.QuadPoint> extends QuadTreeNode<Point> {

	ArrayList<Leaf<Point>> kids = new ArrayList<Leaf<Point>>();

	LeafNode(QuadTree<Point> tree, QuadTreeNode<Point> parent, double llX, double llY, double urX, double urY) {
		super( tree, parent, llX, llY, urX, urY);
		children = null;
	}

	@Override
	ArrayList<Leaf<Point>> boundedChildren( double llx, double lly, double urx, double ury ) {
		ArrayList<Leaf<Point>> retList = new ArrayList<Leaf<Point>>();
		for( Leaf<Point> lf : kids ) {
			if (Set2D.isWithin(lf.leaf, llx, lly, urx, ury)) {
				retList.add(lf);
			}
		}
		return retList;
	}

	/**Removes the leaf from children of THIS if children contain the given leaf
	 * @author Sam Whitlock (cs61b-eo)
	 * @param Leaf<Point>
	 */
	void remove(Leaf<Point> lf) {
		kids.remove(lf);
		if( kids.isEmpty() && parent != null) {
			parent.replace(this, null);
		}
	}
	
	@Override
	void move(Leaf<Point> lf) {
		if( kids.contains(lf) && !Set2D.isWithin(lf.leaf, llx(), lly(), urx(), ury()) && parent != null) {
			parent.move(lf);
			kids.remove(lf);
			if( kids.isEmpty() && parent != null) {
				parent.replace(this, null);
			}
		}
	}

	@Override
	QuadTreeNode<Point> insert(Leaf<Point> lf) {
		if( kids.size() >= max_children ) {
			if( urx() - x() >= myTree.delta && ury() - y() >= myTree.delta ) {
				QuadTreeNode<Point> newNode =new QuadTreeNode<Point>( myTree, parent, llx(), lly(), urx(), ury());
				kids.add(lf);
				for( Leaf<Point> kid : kids ) {
					newNode = newNode.insert(kid);
				}
				
				return newNode;
			} else {
				myTree.leaves.remove(lf.leaf);
				return this;
			}
		} else {
			lf.parent = this;
			kids.add(lf);
			return this;
		}
	}
}
