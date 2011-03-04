package drawing;

/**A Picture representation of a Circle
 * @author Sam Whitlock (cs61b-eo)
 */
class Circle extends Picture {
	protected double radius;
	
	Circle( double offsetX, double offsetY, double rad, double r, double g, double b, double lw ) {
		xOffset = offsetX;
		yOffset = offsetY;
		radius = rad;
		red = r;
		green = g;
		blue = b;
		lineWidth = lw;
	}
	
	/** My value as a Picture.  */
    Picture pictureValue () {
        return this;
    }

    /** Draw me on OUTPUT through a utility
     * @param DrawingUtility
     * @return void
     * */
    void draw(DrawingUtility drawingUtility) {
    	drawingUtility.arc( xOffset, yOffset, radius, 0, 360);
    	drawingUtility.stroke();
	}

    /** New Picture resulting from scaling me by a factor of FACTOR, 
     *  which must be > 0. 
     *  @param double
     *  @return Picture (Circle)
     *  @throws InvalidScalingFactorException
     */
    Picture scale (double factor) throws InvalidScalingFactorException {
    	if( factor > 0 ) {
    		return new Circle( xOffset * factor, yOffset * factor, radius * factor, red, green, blue, lineWidth );
    	}

    	throw new InvalidScalingFactorException(factor);
     }

    /** New Picture resulting from translating me by an amount (X, Y). 
     * @param double x, y
     * @return Picture (Circle)
     */
    Picture translate (double x, double y) {
         return new Circle( xOffset + x, yOffset + y, radius, red, green, blue, lineWidth );
     }

    /** New Picture resulting from rotating me by D degrees counterclockwise.
     *  @param double
     *  @return Picture (circle)
     */
    Picture rotate (double d) {
         return this;
     }
}
