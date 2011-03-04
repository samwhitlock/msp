package drawing;

/** A numeric value in the interpreter.
 *  @author Sam Whitlock (cs61b-eo)
 */
class Number extends Value {

    Number (double val) {
        value = val;
    }

    /** My value as a number.
     * @param void
     * @return double
     * */
    double doubleValue () {
        return value;
    }

    private double value;
}
