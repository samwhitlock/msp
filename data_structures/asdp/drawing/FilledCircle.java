package drawing;

/**A value representing a filled circle
 * @author Sam Whitlock (cs61b-eo)
 */
class FilledCircle extends Circle {
	FilledCircle (double xOffset, double yOffset, double rad, double r, double g, double b, double lw ) {
		super( xOffset, yOffset, rad, r, g, b, lw );
	}
	
	/**Draws the picture through a PostScript translator
	 * @return void
	 * @param DrawingUtility
	 */
	void draw(DrawingUtility drawingUtility) {
		drawingUtility.arc(xOffset, yOffset, radius, 0, 360);
		drawingUtility.fill();
	}
	
	/**New filled circle resulting from scaling the circle
	 * @return Picture
	 * @param double factor
	 * @throws InvalidScalingFactorException
	 */
	Picture scale (double factor) throws InvalidScalingFactorException {
    	if( factor > 0 ) {
    		return new FilledCircle( xOffset * factor, yOffset * factor, radius * factor, red, green, blue, lineWidth );
    	}

    	throw new InvalidScalingFactorException(factor);
     }

   /** New filled circle resulting from translating me by an amount (X, Y). 
    * @return Picture
    * @param double x, y
    */
	Picture translate (double x, double y) {
        return new FilledCircle( xOffset + x, yOffset + y, radius, red, green, blue, lineWidth );
    }

   /** New Picture resulting from rotating me by D degrees counterclockwise.
    *  @return Picture
    *  @param double d
    */
   Picture rotate (double d) {
	   return this;
    }
}
