package drawing;

/**A class representing filled Rectangles
 * @author Sam Whitlock (cs61b-eo)
 */
class FilledRectangle extends Rectangle {
	
	FilledRectangle( double offsetX, double offsetY, double w, double h, double rotAngle, double r, double g, double b, double lw ) {
		super( offsetX, offsetY, w, h, rotAngle, r, g, b, lw );
	}
	
	/**Draws the filled rectangle through a PostScript translator
	 * @param DrawingUtility
	 * @return void
	 */
	@Override
	void draw( DrawingUtility drawingUtility ) {
		frameRotate(drawingUtility);
    	drawingUtility.fill();
	}
	
	/**Returns a scaled version of this picture
	 * @return Picture (filled rectangle)
	 * @param double factor
	 * @throws InvalidScalingFactorException
	 */
	@Override
	Picture scale (double factor) throws InvalidScalingFactorException {
    	if( factor > 0 ) {
    		return new FilledRectangle( xOffset * factor, yOffset * factor, width * factor, height * factor, rotateAngle, red, green, blue, lineWidth );
    	}
    	
    	throw new InvalidScalingFactorException(factor);
     }

   /** New Picture resulting from translating me by an amount (X, Y). 
    * @return Picture (filled rectangle)
    * @param double x, y
    */
	@Override
    Picture translate (double x, double y) {
    	return new FilledRectangle( xOffset + x, yOffset + y, width, height, rotateAngle, red, green, blue, lineWidth );
     }

   /** New Picture resulting from rotating me by D degrees counterclockwise.
    *  @return Picture (filled rectangle)
    *  @param double d
    */
	@Override
   Picture rotate (double d) {
	   return new FilledRectangle( xOffset, yOffset, width, height, rotateAngle + d, red, green, blue, lineWidth);
    }
	
}
