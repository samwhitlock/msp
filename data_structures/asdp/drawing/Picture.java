package drawing;

/** The interface describing all kinds of Pictures.
 *  @author Sam Whitlock (cs61b-eo)
 */
abstract class Picture extends Value {

    /** My value as a Picture.  */
    Picture pictureValue () {
        return this;
    }

    /** Draw me on OUT.  Default implementation does nothing. */
    void draw (DrawingUtility drawingUtility) {
    	throw new IllegalArgumentException("picture cannot be drawn");
    }

    /** New Picture resulting from scaling me by a factor of FACTOR, 
     *  which must be > 0.  Default implementation cannot be scaled. 
     * @throws InvalidScalingFactorException */
    Picture scale (double factor) throws InvalidScalingFactorException {
         throw new IllegalArgumentException ("picture cannot be scaled");
     }

    /** New Picture resulting from translating me by an amount (X, Y). 
     *  Default implementation cannot be translated. */
    Picture translate (double x, double y) {
         throw new IllegalArgumentException ("picture cannot be translated");
     }

    /** New Picture resulting from rotating me by D degrees counterclockwise.
     *  Default implementation cannot be rotated. */
    Picture rotate (double d) {
         throw new IllegalArgumentException ("picture cannot be rotated");
     }

    protected double xOffset;
    protected double yOffset;
    protected double red;
    protected double green;
    protected double blue;
    protected double lineWidth;

}
