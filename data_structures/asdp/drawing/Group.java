package drawing;

import java.util.ArrayList;
import java.util.List;

/**An extension of Picture that represents a group of pictures
 * @author Sam Whitlock (cs61b-eo)
 */
class Group extends Picture {
	protected ArrayList<Picture> picList = new ArrayList<Picture>();

	Group() {
		picList = new ArrayList<Picture>();
	}
	
	Group( ArrayList<Picture> lst ) {
		picList = lst;
	}
	
	List<Picture> getPictureList() {
		return picList;
	}

	/** Draws all of the pictures in the group
	 * @param drawingUtility
	 * @return void
	 */
	void draw (DrawingUtility drawingUtility) {
		if( !picList.isEmpty() ) {
			for( Picture currentPic : picList ) {
				try{
					currentPic.draw(drawingUtility);
				} catch (IllegalStateException e) {
					System.err.print("error : ");
					System.err.println(e.getMessage());
				}
			}
		}
	}

	/**Return a new group in which each member of this group was translated by the same factor
	 * @param double
	 * @return Picture (Group)
	 */
	Picture scale (double factor) {
		ArrayList<Picture> retList = new ArrayList<Picture>();
		if( !picList.isEmpty() ) {
			for( Picture currentPic: picList ) {
				try {
					retList.add(currentPic.scale(factor));
				} catch (IllegalStateException e) {
					System.err.println(e.getMessage());
				} catch (InvalidScalingFactorException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		return new Group( retList );
	}

	/** Returns a group in which each member of this gropu was translated by the same factors
	 * @param double x, y
	 * @return Picture (Group)
	 */
	Picture translate (double x, double y) {
		ArrayList<Picture> retList = new ArrayList<Picture>();
		if( !picList.isEmpty() ) {
			for( Picture currentPic: picList ) {
				try {
					retList.add(currentPic.translate(x, y) );
				} catch (IllegalStateException e) {
					System.err.print("error : ");
					System.err.println(e.getMessage());
				}
			}
		}
		return new Group( retList );
	}

	/** Returns a group that is the result of rotating every Picture in this group by the same factor
	 * @param double
	 * @return Picture (Group)
	 */
	Picture rotate (double d) {
		ArrayList<Picture> retList = new ArrayList<Picture>();
		if( !picList.isEmpty() ) {
			for( Picture currentPic: picList ) {
				try {
					retList.add(currentPic.rotate(d) );
				} catch (IllegalStateException e) {
					System.err.print("error : ");
					System.err.println(e.getMessage());
				}
			}
		}
		return new Group( retList );
	}
}