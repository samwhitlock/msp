package tracker;

import static java.lang.Double.POSITIVE_INFINITY;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import util.QuadTree;

/** The superclass of all types of Command. A Command is described by
 *  a non-numeric S-expression, as indicated in the specifications.
 *  @author Sam Whitlock (cs61b-eo)
 */
class Command {

	private static double radius;
	private static double specialRadius;
	private static QuadTree<QPoint> tree;
	private static HashMap<Integer, QPoint> set;
	

	static {
		radius = POSITIVE_INFINITY;
		tree = new QuadTree<QPoint>( 0, 0, 0, 0, radius );
		set = new HashMap<Integer, QPoint>();
	}

	/**Adds an instance of QPoint to the tree that exists in THIS
	 * @author Sam Whitlock (cs61b-eo)
	 * @param str[] of command and args
	 */
	static void add(String[] str) {
		if( str.length == 6) {
			int ID;
			double x, y, xVel, yVel;
			try {
				ID = Integer.parseInt(str[1]);
				x = Double.parseDouble(str[2]);
				y = Double.parseDouble(str[3]);
				xVel = Double.parseDouble(str[4]);
				yVel = Double.parseDouble(str[5]);
			} catch (NumberFormatException e) {
				Debug.badNumberFormat(str);
				return;
			}

			if( ID >= 0 ) {
				if( !set.containsKey(ID) ) {
					QPoint newP = new QPoint( ID, x, y, xVel, yVel );
					
					tree.add(newP);
					if( tree.contains(newP) ) {
						set.put(ID, newP);
					}
				} else {
					Debug.idAlreadyPresent(ID);
				}
			} else {
				Debug.negativeID(ID);
			}
		} else {
			Debug.badArgNum( str.length - 1, 5 );
		}
	}

	/**Sets the bounds of the tree that exists in THIS
	 * and moves any points from the old tree to the new tree
	 * @author Sam Whitlock (cs61b-eo)
	 * @param str array of command and args
	 */
	static void bounds(String[] str) {
		if( str.length == 5 ) {
			double llX, llY, urX, urY;
			try {
				llX = Double.parseDouble(str[1]);
				llY = Double.parseDouble(str[2]);
				urX = Double.parseDouble(str[3]);
				urY = Double.parseDouble(str[4]);
			} catch (NumberFormatException e) {
				Debug.badNumberFormat(str);
				return;
			}

			if( llX <= tree.llx() && llY <= tree.lly() && urX >= tree.urx() && urY >= tree.ury() && llX <= urX && llY <= urY) {
				QuadTree<QPoint> newTree = new QuadTree<QPoint>( llX, llY, urX, urY, radius);
				tree.moveTo(newTree);
				tree = newTree;
			} else {
				Debug.cannotShrinkBounds();
			}	
		} else {
			Debug.badArgNum( str.length - 1, 4 );
		}
	}

	/** Sets the radius of the points that exist in the tree in THIS
	 * and move all the points to a new tree with the new given radius
	 * @param str[] of command and arguments
	 * @author Sam Whitlock (cs61b-eo)
	 */
	static void rad(String[] str) {
		if( str.length == 2 ) {
			double newRad;
			try {
				newRad = Double.parseDouble(str[1]);
			} catch (NumberFormatException e) {
				Debug.badNumberFormat(str[1]);
				return;
			}

			if( newRad <= radius ) {
				radius = newRad;
				QuadTree<QPoint> newTree = new QuadTree<QPoint>( tree.llx(), tree.lly(), tree.urx(), tree.ury(), radius);
				tree.moveTo(newTree);
				tree = newTree;
			} else {
				Debug.cannotIncreaseRadius(radius, newRad);
			}
		} else {
			Debug.badArgNum( str.length - 1, 1 );
		}
	}

	/**Loads a new file by pushing a new FileReader onto the 
	 * stack of Scanners in the Main class
	 * @author Sam Whitlock (cs61b-eo)
	 * @param string array of command and arguments
	 */
	static void load(String[] str) {
		if( str.length == 2 ) {
			FileReader loadFile;
			try {
				loadFile = new FileReader(str[1]);
			} catch (FileNotFoundException e) {
				Debug.cannotReadFile(str[1]);
				return;
			}

			Main.inputs.push(new Scanner(loadFile));
			Main.printPrompt = false;
		} else {
			Debug.badArgNum( str.length - 1, 1 );
		}
	}

	/**Outputs the closer-than command based on the specifications
	 * in User-Manual
	 * @author Sam Whitlock (cs61b-eo)
	 * @param str
	 */
	static void closerThan(String[] str) {
		if( str.length == 2 ) {
			double rad;
			try {
				rad = Double.parseDouble(str[1]);
			} catch (NumberFormatException e) {
				Debug.badNumberFormat(str[1]);
				return;
			}

			if( rad >= 2 * radius  ) {
				Iterator<QPoint> iter = tree.iterator();
				while( iter.hasNext() ) {
					QPoint pt = iter.next();
					Iterator<QPoint> innerIterator = tree.iterator( pt.x() - rad, pt.y() - rad, pt.x() + rad, pt.y() + rad );
					while( innerIterator.hasNext() ) {
						QPoint pnt = innerIterator.next();
						if( radiusCheck( pt, pnt, rad ) ) {
							if( pt.id() < pnt.id() ) {
								System.out.printf("%d:(%.4g,%.4g,%.4g,%.4g) ", pt.id(), pt.x(), pt.y(), pt.xVelocity(), pt.yVelocity() );
								System.out.printf("%d:(%.4g,%.4g,%.4g,%.4g)\n", pnt.id(), pnt.x(), pnt.y(), pnt.xVelocity(), pnt.yVelocity() );
							}
						}
					}
				}
				System.out.println();
			}
		} else {
			Debug.badArgNum( str.length - 1, 1 );
		}
	}

	/**Checks to see if 2 objects are at least distance apart, and return true if so
	 * @author Sam Whitlock (cs61b-eo)
	 * @param pt1
	 * @param pt2
	 * @param dist
	 * @return
	 */
	private static boolean radiusCheck(QPoint pt1, QPoint pt2, double dist) {
		return radiusCheck( pt1.x(), pt1.y(), pt2.x(), pt2.y(), dist);
	}

	/**Checks to see if 2 objects are at least distance apart, and return true if so
	 * @author Sam Whitlock (cs61b-eo)
	 * @param pt
	 * @param x
	 * @param y
	 * @param dist
	 * @return
	 */
	private static boolean radiusCheck(QPoint pt, double x, double y, double dist ) {
		return radiusCheck( pt.x(), pt.y(), x, y, dist );
	}

	/**Checks to see if 2 objects are at least distance apart, and return true if so
	 * @author Sam Whitlock (cs61b-eo)
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param dist
	 * @return
	 */
	private static boolean radiusCheck( double x1, double y1, double x2, double y2, double dist) {
		return dist >= Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

	/**Writes the current state of all the points in the
	 * tree to a file based on the specifications outlined in
	 * User-Manual
	 * @author Sam Whitlock (cs61b-eo)
	 * @param str array of command and args
	 */
	static void write(String[] str) {
		if( str.length == 2 ) {
			BufferedWriter output;
			try {
				output = new BufferedWriter(new FileWriter(str[1]));

				output.write("bounds " + tree.llx() + " " + tree.lly() + " " + tree.urx() + " " + tree.ury() + "\n");
				output.write("rad " + radius + "\n");

				ArrayList<QPoint> setList = new ArrayList<QPoint>(set.values());
				Collections.sort(setList);

				for ( QPoint p : setList ) {
					output.write(
							"add " + 
							p.id() + " " + 
							p.x() + " " + 
							p.y() + " " +
							p.xVelocity() + " " +
							p.yVelocity() + "\n"
					);
				}

				output.close();
			} catch (IOException e) {
				Debug.cannotWriteToFile(str[1]);
			}	
		} else {
			Debug.badArgNum( str.length - 1, 1 );
		}
	}

	/**Implements the near command based on the specifications outlined
	 * in User-Manual
	 * @param str array of command and arguments
	 * @author Sam Whitlock (cs61b-eo)
	 */
	static void near(String[] str) {
		if( str.length == 4 ) {
			double llX, llY, urX, urY, dist, x = 0, y = 0;
			boolean ignoreRadius = true;
			try {
				dist = Double.parseDouble(str[3]);
			} catch (NumberFormatException e) {
				Debug.badNumberFormat(str[3]);
				return;
			}

			if( str[1].compareTo("*") == 0 ) {
				try {
					llX = tree.llx();
					urX = tree.urx();
					double temp = Double.parseDouble(str[2]);
					llY = temp - dist;
					urY = temp + dist;
				} catch (NumberFormatException e) {
					Debug.badNumberFormat(str[2]);
					return;
				}
			} else if (str[2].compareTo("*") == 0) {
				try {
					llY = tree.lly();
					urY = tree.ury();
					double temp = Double.parseDouble(str[1]);
					llX = temp - dist;
					urX = temp + dist;
				} catch (NumberFormatException e) {
					Debug.badNumberFormat(str[1]);
					return;
				}
			} else {
				try {
					x = Double.parseDouble(str[1]); y = Double.parseDouble(str[2]);
					llX = x - dist;
					llY = y - dist;
					urX = x + dist;
					urY = y + dist;
				} catch (NumberFormatException e) {
					Debug.badNumberFormat(new String[] { str[1], str[2] });
					return;
				}
				ignoreRadius = false;
			}

			Iterator<QPoint> iter = tree.iterator(llX, llY, urX, urY);
			ArrayList<QPoint> pointsToPrint = new ArrayList<QPoint>();

			while( iter.hasNext() ) {
				QPoint p = iter.next();
				if( ignoreRadius || radiusCheck( p, x, y, dist ) ) {
					pointsToPrint.add(p);
				}
			}

			Collections.sort(pointsToPrint);

			for( int i = 0; i < pointsToPrint.size(); i++ ) {
				QPoint pt = pointsToPrint.get(i);
				System.out.printf("%d:(%.4g,%.4g,%.4g,%.4g)", pt.id(), pt.x(), pt.y(), pt.xVelocity(), pt.yVelocity() );
				if( i % 2 != 0 ) {
					if( i < pointsToPrint.size() -1 )
					System.out.println();
				} else {
					System.out.print(" ");
				}
			}

			System.out.println();
		} else {
			Debug.badArgNum( str.length - 1, 3 );
		}
	}

	/**Implements the simulate command based on the specifications
	 * outlined in User-Manual
	 * @param string array of command and its arguments
	 * @author Sam Whitlock (cs61b-eo)
	 */
	static void simulate(String[] str) {
		if( str.length == 2 ) {
			double time;
			try {
				time = Double.parseDouble(str[1]);
			} catch (NumberFormatException e) {
				Debug.badNumberFormat(str[1]);
				return;
			}

			double D = 2 * radius;
			specialRadius = 4 * (1.0 + 1.0 / (1L << 50)) * Math.pow(radius, 2);
			while ( time > 0 ) {
				double maxVelocity = 0, minTime = POSITIVE_INFINITY;

				Iterator<QPoint> iter = tree.iterator();
				while( iter.hasNext() ) {
					QPoint next = iter.next();
					maxVelocity = Math.max(maxVelocity, Math.pow(next.xVelocity(), 2) + Math.pow(next.yVelocity(), 2));
				}
				
				maxVelocity = Math.sqrt(maxVelocity);

				iter = tree.iterator();
				while( iter.hasNext() ) {
					QPoint next = iter.next();
					Iterator<QPoint> innerIter = tree.iterator(next.x() - 6 * radius, next.y() - 6 * radius, next.x() + 6 * radius, next.y() + 6 * radius);
					minTime = Math.min(minTime, timeToWall(next));
					while( innerIter.hasNext() ) {
						QPoint innerNext = innerIter.next();
						if ( next != innerNext ) {
							minTime = Math.min(minTime, ucb.proj2.Physics.collide(new double[] { next.x(), next.y(), next.xVelocity(), next.yVelocity() }, new double[] { innerNext.x(), innerNext.y(), innerNext.xVelocity(), innerNext.yVelocity() }, radius));
						}
					}
				}
				
				minTime = Math.min(minTime, Math.min(time, D / maxVelocity));

				iter = tree.iterator();
				while( iter.hasNext() ) {
					QPoint pt = iter.next();
					pt.move(pt.x() + (minTime * pt.xVelocity), pt.y() + (minTime * pt.yVelocity));
				}

				iter = tree.iterator();
				while(iter.hasNext()) {
					QPoint pt = iter.next();
					int wallNum = collidesWithWall(pt);
					if( wallNum > 0 ) {
						if( wallNum == 1 && pt.xVelocity != 0) {
							pt.xVelocity = -1 * pt.xVelocity;
						} else if (wallNum == 2 && pt.xVelocity != 0){
							pt.yVelocity = -1 * pt.yVelocity;
						} else {
							pt.xVelocity = -1 * pt.xVelocity;
							pt.yVelocity = -1 * pt.yVelocity;
						}
					} else {
						Iterator<QPoint> innerIter = tree.iterator(pt.x() - (2.5 * radius), pt.y() - (2.5 * radius), pt.x() + (2.5 * radius), pt.y() * (2.5 * radius));
						while( innerIter.hasNext() ) {
							QPoint innerNext = innerIter.next();
							if( pt != innerNext && willCollide(pt, innerNext)) {
								double[] PT = new double[] {pt.x(), pt.y(), pt.xVelocity(), pt.yVelocity() };
								double[] IN = new double[] { innerNext.x(), innerNext.y(), innerNext.xVelocity(), innerNext.yVelocity() };

								ucb.proj2.Physics.rebound(PT, IN);
								pt.xVelocity = PT[2]; pt.yVelocity = PT[3];
								innerNext.xVelocity = IN[2]; innerNext.yVelocity = IN[3];
							}
						}
					}
				}

				time -= minTime;
			}
		} else {
			Debug.badArgNum( str.length - 1, 1 );
		}
	}

	/**Determines the time to the nearest wall collision for a 
	 * point. Does not take into account any interactions
	 * with point between it and any wall
	 * @param QPoint
	 * @return the time to the nearest wall based on its velocity (double)
	 */
	private static double timeToWall(QPoint next) {
		double xTime = POSITIVE_INFINITY, yTime = POSITIVE_INFINITY;
		if( next.xVelocity > 0 ) {
			xTime = ((tree.urx() - next.x()) - radius) / next.xVelocity;
		} else if( next.xVelocity < 0 ) {
			xTime = ((next.x() - tree.llx()) - radius) / ( -1 * next.xVelocity );
		}
		if( next.yVelocity > 0 ) {
			yTime = ((tree.ury() - next.y()) - radius) / next.yVelocity;
		} else if( next.yVelocity < 0 ) {
			yTime = ((next.y() - tree.llx()) - radius) / (-1 * next.yVelocity);
		}
		return Math.min(xTime, yTime);
	}

	/**Returns true if objects are colliding
	 * An error factor is introduced to take into account
	 * instances when the collide method in Physics returns something considered
	 * to be too small so that movement essentially halts. This often happens when points are
	 * moving at each other at a very fast velocity and they have very small radii.
	 * @author Sam Whitlock (cs61b-eo)
	 * @param QPoint
	 * @param QPoint
	 * @return true if they are colliding
	 */
	private static boolean willCollide(QPoint p1, QPoint p2) {
		double time =  ucb.proj2.Physics.collide(
				new double[] {p1.x(), p1.y(), p1.xVelocity, p1.yVelocity },
				new double[] { p2.x(), p2.y(), p2.xVelocity, p2.yVelocity },
				radius
		);
		return (specialRadius >= Math.pow(p1.x() - p2.x(), 2) + Math.pow(p1.y() - p2.y(), 2) && POSITIVE_INFINITY > time) ||
		time <= Math.pow(10, -14);
	}

	/**Returns a integer that represents which walls the particle
	 * is colliding with so that the simulate method may invert the appropriate velocities
	 * @param QPoint
	 * @return int
	 * @author Sam Whitlock (cs61b-eo)
	 */
	private static int collidesWithWall(QPoint pt) {
		boolean xWall = false, yWall = false;
		xWall = Math.pow(pt.x()-tree.llx(), 2) <= specialRadius / 2 || Math.pow(tree.urx() - pt.x(), 2) <= specialRadius / 2;
		yWall = Math.pow(pt.y()-tree.lly(), 2) <= specialRadius / 2 || Math.pow(tree.ury() - pt.y(), 2) <= specialRadius / 2;

		if( xWall && yWall ) {
			return 3;
		} else if( xWall ) {
			return 1;
		} else if( yWall ) {
			return 2;
		} else return 0;
	}

	/**Prints out a help command as 
	 * specified by User-Manual
	 * @param str of command and arguments
	 * @author Sam Whitlock (cs61b-eo)
	 */
	static void help(String[] str) {
		if( str.length == 1 ) {
			Debug.help();
		} else {
			Debug.badArgNum( str.length - 1, 0 );
		}
	}

	/**Quits the program by calling the exit method in main
	 * @author Sam Whitlock (cs61b-eo)
	 * @param str of command and arguments
	 */
	static void quit(String[] str) {
		if( str.length == 1 ) {
			Main.exit();
		} else {
			Debug.badArgNum( str.length - 1, 0 );
		}
	}
}

