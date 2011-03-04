package drawing;

/**A class that represents a Rectangle
 * @author Sam Whitlock (cs61b-eo)
 */
class Rectangle extends Picture {
	protected double width;
	protected double height;
	protected double rotateAngle;
	
	Rectangle( double offsetX, double offsetY, double w, double h, double rotAngle, double r, double g, double b, double lw ) {
		xOffset = offsetX;
		yOffset = offsetY;
		width = w;
		height = h;
		rotateAngle = rotAngle;
		red = r;
		green = g;
		blue = b;
		lineWidth = lw;
		
	}
	
	/** Draw me on OUT.  Default implementation does nothing.
	 * @param DrawingUtility
	 * @return void
	 * */
    void draw (DrawingUtility drawingUtility) {
    	frameRotate(drawingUtility);
    	drawingUtility.stroke();
    }

    /** New Picture resulting from scaling me by a factor of FACTOR, 
     *  which must be > 0.
     *  @throws InvalidScalingFactorException
     *  @return Picture
     *  @param double
     */
    Picture scale (double factor) throws InvalidScalingFactorException {
    	if( factor > 0 ) {
    		return new Rectangle( xOffset * factor, yOffset * factor, width * factor, height * factor, rotateAngle, red, green, blue, lineWidth );
    	}
    	
    	throw new InvalidScalingFactorException(factor);
     }

    /** New Picture resulting from translating me by an amount (X, Y). 
     *  @return Picture (Rectangle)
     *  @param double x, y
     */
    Picture translate (double x, double y) {
    	return new Rectangle( xOffset + x, yOffset + y, width, height, rotateAngle, red, green, blue, lineWidth );
     }

    /** New Picture resulting from rotating me by D degrees counterclockwise.
     * @param double d
     * @return Picture (Rectangle)
     */
    Picture rotate (double d) {
    	return new Rectangle( xOffset, yOffset, width, height, rotateAngle + d, red, green, blue, lineWidth );
     }
    
    /**The common rectangle PostScript constructor (because except for fill and what not, it's the same)
     * @return void
     * @param drawingUtility
     */
    protected void frameRotate(DrawingUtility drawingUtility) {
    	drawingUtility.moveTo(xOffset, yOffset);
    	drawingUtility.rLineTo(width * Math.cos(Math.toRadians(rotateAngle)), width * Math.sin(Math.toRadians(rotateAngle)));
    	drawingUtility.rLineTo(-height * Math.sin(Math.toRadians(rotateAngle)), height * Math.cos(Math.toRadians(rotateAngle)));
    	drawingUtility.rLineTo( -width * Math.cos(Math.toRadians(rotateAngle)), -width * Math.sin(Math.toRadians(rotateAngle)));
    	drawingUtility.lineTo(xOffset, yOffset);
    }
}
