package util;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.ArrayList;
import java.util.Iterator;

import util.QuadTree.QuadPoint;

/** Central dispatching point for all testing.  This is essentially a wrapper
 * @author Sam Whitlock (cs61b-eo)
 * */
public class Testing {

    /** Run the JUnit tests in the tracker package. */
    public static void main (String[] ignored) {
        textui.runClasses (tracker.Testing.class);
    }

    /** Test that hasNext works on empty tree.
     * @param void
     * @return void
     * */
    @Test public void testEmpty1 () {
        Set2D<QuadPoint> set = 
            new QuadTree<QuadPoint> (-1.0, -1.0, 1.0, 1.0, 0.0001);

        assertEquals ("size 0", 0, set.size (), 0.0);
        assertEquals ("llx", -1.0, set.llx (), 0.0);
        assertEquals ("lly", -1.0, set.lly (), 0.0);
        assertEquals ("urx", 1.0, set.urx (), 0.0);
        assertEquals ("ury", 1.0, set.ury (), 0.0);

        Iterator<QuadPoint> i = set.iterator ();
        assertTrue ("nothing in set", ! i.hasNext ());
    }

   //leaf tests
    /** testing leaf
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     * */
    @Test public void leafTest1() {
    	QuadPoint qp = new QuadPoint(13, 19);
    	Leaf<QuadTree.QuadPoint> myPt = new Leaf<QuadTree.QuadPoint>(qp, null, null);
    	assertTrue(myPt.x() == 13);
    	assertTrue(myPt.y() == 19);
    	assertEquals( qp, myPt.leaf);
    	LeafNode<QuadPoint> lfnd = new LeafNode<QuadPoint>(null, null, 0, 0, 100, 100);
    	myPt.parent = lfnd;
    	assertEquals(myPt.parent = lfnd);
    	QuadTree<QuadPoint> tree = new QuadTree<QuadPoint>(0, 0, 100, 100, .01);
    	myPt.tree = tree;
    	assertEquals(myPt.tree, tree);
    }

    /**Testing the instance variables of the quadtree
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     * */
    @Test public void treeTest1() {
    	QuadTree<QuadPoint> tree = new QuadTree<QuadPoint>( -5, -15, 99, 107, .001);
    	assertTrue(tree.radius == .001);
    	assertTrue(tree.llx() == -5);
    	assertTrue(tree.lly() == -15);
    	assertTrue(tree.urx() == 99);
    	assertTrue(tree.ury() == 107);
    }
    
    /**some basic tests of QuadTree representation
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     * */
    @Test public void treeTest2() {
    	QuadTree<QuadPoint> tree = new QuadTree<QuadPoint>( -5, -15, 99, 107, .001);
    	QuadPoint pt = new QuadPoint(4, 4);
    	tree.add(pt);
    	assertTrue( tree.contains(pt) );
    	assertTrue( tree.size() == 1);
    	
    	tree.remove(pt);
    	assertTrue( !tree.contains(pt));
    	assertTrue( tree.size() == 1);
    }
    
    /**Testing the moveTo method in QuadTree
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     * */
    @Test public void treeTest3() {
    	QuadTree<QuadPoint> tree = new QuadTree<QuadPoint>( -5, -15, 99, 107, .001);
    	QuadTree<QuadPoint> tree2 = new QuadTree<QuadPoint>( -6, -16, 100, 117, 1.101);
    	QuadPoint pt = new QuadPoint(51, 23);
    	
    	tree.add(pt);
    	assertTrue( tree.contains(pt) );
    	assertTree( tree.size() == 1);
    	assertTrue( !tree2.contains(pt) );
    	assertTrue( tree2.size() == 0);
    	
    	tree.moveTo(tree2);
    	assertTrue( !tree.contains(pt) );
    	assertTree( tree.size() == 0);
    	assertTrue( tree2.contains(pt) );
    	assertTrue( tree2.size() == 1);
    }
    
    /**Testing simple adding and removing
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     * */
    @Test public void treeTest4() {
    	QuadTree<QuadPoint> tree = new QuadTree<QuadPoint>( -6, -16, 100, 117, 1.101);
    	QuadPoint pt = new QuadPoint(51, 23);
    	
    	assertTrue( !tree.contains(pt));
    	assertTrue( tree.size() == 0);
    	
    	tree.add(pt);
    	assertTrue( tree.contains(pt));
    	assertTrue( tree.size() == 1);
    	
    	tree.remove(pt);
    	assertTrue( !tree.contains(pt));
    	assertTrue( tree.size() == 0);
    }
    
    /**Testing the non-bounded iterator
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     */
    @Test public void treeTest5() {
    	QuadTree<QuadPoint> tree = new QuadTree<QuadPoint>(-10, -10, 10, 10, .5);
    	QuadPoint pt1 = new QuadPoint(-7, 3);
    	QuadPoint pt2 = new QuadPoint(5, 5);
    	QuadPoint pt3 = new QuadPoint( 9, 9);
    	QuadPoint pt4 = new QuadPoint(0, 0);
    	
    	tree.add(pt1); tree.add(pt2); tree.add(pt3); tree.add(pt4);
    	
    	Iterator<QuadPoint> iter = tree.iterator();
    	ArrayList<QuadPoint> retList = new ArrayList<QuadPoint>();
    	while( iter.hasNext() ) {
    		retList.add(iter.next());
    	}
    	
    	assertTrue(retList.size() == 4);
    	assertTrue(retList.contains(pt1));
    	assertTrue(retList.contains(pt2));
    	assertTrue(retList.contains(pt3));
    	assertTrue(retList.contains(pt4));
    }
    
    /**Testing the bounded iterator
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     */
    @Test public void treeTest6() {
    	QuadTree<QuadPoint> tree = new QuadTree<QuadPoint>(-10, -10, 10, 10, .5);
    	QuadPoint pt1 = new QuadPoint(1, 1);
    	QuadPoint pt2 = new QuadPoint(9, 9);
    	QuadPoint pt3 = new QuadPoint(-5, 1);
    	QuadPoint pt4 = new QuadPoint( -7, 2);
    	QuadPoint pt5 = new QuadPoint(-4, -4);
    	QuadPoint pt6 = new QuadPoint( -7, -6);
    	QuadPoint pt7 = new QuadPoint(6, -2);
    	QuadPoint pt8 = new QuadPoint(8, -4);
    	
    	tree.add(pt1); tree.add(pt2); tree.add(pt3); tree.add(pt4); tree.add(pt5); tree.add(pt6); tree.add(pt7); tree.add(pt8);
    	
    	ArrayList<QuadPoint> retList = new ArrayList<QuadPoint>();
    	Iterator<QuadPoint> iter = tree.iterator(-10, -10, 10, 10);
    	while(iter.hasNext()) {
    		retList.add(iter.next());
    	}
    	
    	assertTrue(retList.size() == 8);
    	assertTrue(retList.contains(pt1));
    	assertTrue(retList.contains(pt2));
    	assertTrue(retList.contains(pt3));
    	assertTrue(retList.contains(pt4));
    	assertTrue(retList.contains(pt5));
    	assertTrue(retList.contains(pt6));
    	assertTrue(retList.contains(pt7));
    	assertTrue(retList.contains(pt8));
    	
    	retList.clear();
    	iter = tree.iterator(0, 0, 10, 10);
    	while(iter.hasNext()) {
    		retList.add(iter.next());
    	}
    	assertTrue(retList.size() == 2);
    	assertTrue(retList.contains(pt1));
    	assertTrue(retList.contains(pt2));
    	
    	retList.clear();
    	iter = tree.iterator(-10, 0, 0, 10);
    	while(iter.hasNext()) {
    		retList.add(iter.next());
    	}
    	assertTrue(retList.size() == 2);
    	assertTrue(retList.contains(pt3));
    	assertTrue(retList.contains(pt4));
    	
    	retList.clear();
    	iter = tree.iterator(-10, -10, 0, 0);
    	while(iter.hasNext()) {
    		retList.add(iter.next());
    	}
    	assertTrue(retList.size() == 2);
    	assertTrue(retList.contains(pt5));
    	assertTrue(retList.contains(pt6));
    	
    	retList.clear();
    	iter = tree.iterator(0, -10, 10, 0);
    	while(iter.hasNext()) {
    		retList.add(iter.next());
    	}
    	assertTrue(retList.size() == 2);
    	assertTrue(retList.contains(pt7));
    	assertTrue(retList.contains(pt8));    	
    }
    
    /**Testing LeafNode methods. Because LeafNode inherits from QuadTreeNode, bounds on the node will be tested with QuadTreeNode
     * because none of those methods are overridden in LeafNode
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     */
    @Test leafNodeTest1() {
    	LeafNode<QuadPoint> node = new LeafNode<QuadPoint>(null, null, 0, 0, 10, 10);
    	QuadPoint pt1 = new QuadPoint(1, 1);
    	QuadPoint pt2 = new QuadPoint(9, 9);
    	Leaf<QuadPoint> lf1 = new Leaf<QuadPoint>(pt1, null, null);
    	Leaf<QuadPoint> lf2 = new Leaf<QuadPoint>(pt2, null, null);
    	
    	node.insert(lf1); node.insert(lf2);
    	assertTrue(node.kids.contains(lf1) && node.kids.contains(lf2));
    	
    	ArrayList<Leaf<QuadPoint>> boundedKids = node.boundedChildren(0, 0, 10, 10);
    	assertTrue(boundedKids.contains(lf1) && boundedKids.contains(lf2));
    	
    	boundedKids.clear();
    	
    	boundedKids = node.boundedChildren(0, 0, 3, 3);
    	assertTrue(boundedKids.contains(lf1) && !boundedKids.contains(lf2));
    	
    	boundedKids.clear();
    	boundedKids = node.boundedChildren(7, 7, 10, 10);
    	assertTrue(boundedKids.contains(lf2) && !boundedKids.contains(lf1));
    	
    	boundedKids.clear();
    	boundedKids = node.boundedChildren(3, 3, 7, 7);
    	assertTrue(boundedKids.isEmpty());
    	
    	node.remove(lf1); node.remove(lf2);
    	assertTrue(node.kids.isEmpty());
    }
    
    /**Testing the public members of QuadTreeNode
     * @author Sam Whitlock (cs61b-eo)
     * @param void
     * @return void
     * */
    @Test QTNtest1() {
    	QuadTreeNode<QuadPoint> qnt = new QuadTreeNode<QuadPoint>(null, null, -1, 0, 10, 11);
    	assertEquals( qnt.llx(), -1);
    	assertEquals( qnt.lly(), 0);
    	assertEquals( qnt.urx(), 10);
    	assertEquals( qnt.ury(), 11);
    	assertEquals( qnt.x(), 11);
    	assertEquals( qnt.y(), 11);
    	QuadTreeNode<QuadPoint> qnt1 = new QuadTreeNode<QuadPoint>(null, null, 0, 0, 10, 10);
    	qnt.parent = qnt1;
    	assertEquals( qnt1, qnt.parent);
    	QuadTree<QuadPoint> tree = new QuadTree<QuadPoint>(-90, -90, 90, 90, 5);
    	qnt.myTree = tree;
    	assertEquals(qnt.myTree, tree);
    }
}
