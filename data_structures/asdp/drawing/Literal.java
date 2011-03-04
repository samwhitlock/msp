package drawing;

/** A Command that corresponds to a numeral in a program, such as the
 *  numeral 3.0 in the statement (:= x 3.0).
 *  @author Sam Whitlock (cs61b-eo)
 */
class Literal extends Command {

    /** A Literal whose value is VALUE. */
    Literal (double val) {
        value = new Number (val);
    }

    @Override
    /** When executed, a Literal simply yields the Number formed by
     *  the argument to its constructor. */
    Value execute (Interpreter machine) {
        return value;
    }

    private Value value;
}

