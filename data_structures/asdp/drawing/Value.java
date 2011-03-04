package drawing;

/** Superclass describing values manipulated by the drawing 
 *  system (stored into variables, or passed as function arguments).
 *  Provides default implementations for catching errors.
 *  @author Sam Whitlock (cs61b-eo)
 */
abstract class Value {
    
    /** My value as a number.  Throws IllegalStateException if I am 
     *  not a number.  Default implementation.
     * @throws IllegalStateException if Value is not of Type Number (or doesn't have a double value associated with it)  
     */
    double doubleValue () {
        throw new IllegalStateException ("value is not a number");
    }

    /** My value as a Picture.  Throws IllegalStateException if I am not
     *  a Picture.  Default implementation. 
     *  @throws IllegalStateException if Value is not of Type Picture
     */
    Picture pictureValue () {
        throw new IllegalStateException ("value is not a picture");
    }
    
    /** @author Sam Whitlock (cs61b-eo)
     * My value as a String. Implemented to deal with implementing Symbol and SymbolCommand
     * @throws IllegalStateException if Value not of Type Picture
     */
    String stringValue() {
    	throw new IllegalStateException("value is not a string");
    }

}

    
