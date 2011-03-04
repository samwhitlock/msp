package drawing;

import java.util.ArrayList;

/** A command that returns a value that is the result of dividing its 2 operands
 * @author Sam Whitlock (cs61b-eo)
 */
class DivideCommand extends Command {

	DivideCommand() {
		maxOperands = 2;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (Number)
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return new Number(lookupCommand(operands.get(0), machine).execute(machine).doubleValue() / lookupCommand(operands.get(1), machine).execute(machine).doubleValue());
		}
		System.err.println("DivideCommand cannot return a valid value and the program must exit.");
		throw new IllegalNumberOfArgumentsException(operands.size(), "Divide");
	}
}
