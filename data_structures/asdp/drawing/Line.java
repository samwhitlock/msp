package drawing;

/**A Picture representation of a line
 * @author Sam Whitlock (cs61b-eo)
 */
class Line extends Picture {

	protected double xStop;
	protected double yStop;
	
	Line( double xInit, double yInit, double xFinal, double yFinal, double r, double g, double b, double lw ) {
		xOffset = xInit;
		yOffset = yInit;
		xStop = xFinal;
		yStop = yFinal;
		red = r;
		green = g;
		blue = b;
		lineWidth = lw;
	}
	
    /** Draws the picture through a PostScript interpreter
     * @param DrawingUtility
     * @return void
     */
	void draw (DrawingUtility drawingUtility) {
		drawingUtility.moveTo(xOffset, yOffset);
		drawingUtility.lineTo(xStop, yStop);
		drawingUtility.stroke();
	}

	/** New Picture resulting from scaling me by a factor of FACTOR, 
     *  which must be > 0.'
     * @return Picture (Line)
     * @param double factor 
     */
    Picture scale (double factor) throws InvalidScalingFactorException {
    	if ( factor > 0 ) {
    		return new Line (xOffset * factor, yOffset * factor, xStop * factor, yStop * factor, red, green, blue, lineWidth );
    	}

    	throw new InvalidScalingFactorException(factor);
     }

    /** New Picture resulting from translating me by an amount (X, Y). 
     * @return Picture (Line)
     * @param double x, y 
     */
    Picture translate (double x, double y) {
    	return new Line (xOffset + x, yOffset + y, xStop + x, yStop + y, red, green, blue, lineWidth );
     }

    /** New Picture resulting from rotating me by D degrees counterclockwise.
     *  @param double d
     *  @return Picture (Line)
     */  
    Picture rotate (double d) {
    	return new Line(
    			xOffset,
    			yOffset,
    			(xStop * Math.cos(Math.toRadians(d))) - (yStop * Math.sin(Math.toRadians(d))),
    			(xStop * Math.sin(Math.toRadians(d))) + (yStop * Math.cos(Math.toRadians(d))),
    			red, green, blue, lineWidth
    			);
     }	
}
