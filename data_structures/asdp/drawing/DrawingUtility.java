package drawing;

import java.io.PrintWriter;

/** A class that centralizes all the drawing and output to the PrintWriter
 * @author Sam Whitlock (cs61b-eo)
 */
class DrawingUtility {
	private PrintWriter output;
	
	/**Constructor must take a PrintWriter
	 * @param PrintWriter
	 */
	DrawingUtility( PrintWriter out ) {
		output = out;
	}
	
	/**Output the moveto PostScript command
	 * @param Double x, Double y
	 * @return void
	 */
	void moveTo( double x, double y ) {
		output.println( "" + x + " " + y + " moveto");
	}
	
	/**Output the lineto PostScript command
	 * @param Double x, Double y
	 * @return void
	 */
	void lineTo( double x, double y) {
		output.println( "" + x + " " + y +  " lineto");
	}
	
	/**Output the rmoveto PostScript command
	 * @param Double x, Double y
	 * @return void
	 */
	void rMoveTo( double delX, double delY ) {
		output.println( "" + delX + " " + delY + " rmoveto");
	}
	
	/**Output the rlineto PostScript command
	 * @param Double x, Double y
	 * @return void
	 */
	void rLineTo( double delX, double delY ) {
		output.println( "" + delX + " " + delY + " rlineto");
	}
	
	/**Output the arc PostScript command
	 * @param Double x, y, radius, initalAngle, finalAngle
	 * @return void
	 */
	void arc( double xPos, double yPos, double radius, double thetaInit, double thetaFinal ) {
		output.println( "" + xPos + " " + yPos + " " + radius + " " + thetaInit + " "  + thetaFinal + " arc");
	}
	
	/**Output the stroke PostScript command
	 * @param void
	 * @return void
	 */
	void stroke() {
		output.println("stroke");
	}
	
	/**Output the fill PostScript command
	 * @param void
	 * @return void
	 */
	void fill() {
		output.println("fill");
	}
	
	/**Output the setrgbcolor PostScript command
	 * @param Double red, green, blue
	 * @return void
	 */
	void setRGBcolor(double red, double green, double blue) {
		output.println("" + red + " " + green + " " + blue + " setrgbcolor");
	}
	
	
	/**Output the setlinewidth PostScript command
	 * @param Double lineWidth
	 * @return void
	 */
	void setLineWidth(double lineWidth) {
		output.println("" + lineWidth + " linewidth");
	}
}
