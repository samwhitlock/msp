package drawing;

import java.util.ArrayList;

/** A command representing the subtraction of 2 Numbers.
 * @author Sam Whitlock (cs61b-eo)
 */
class SubtractCommand extends Command {

	SubtractCommand() {
		maxOperands = 2;
		operands = new ArrayList<Command>();
	}
	
	/**Override of execute method from Command. Returns a Number.
	 * @return Value
	 * @param Interpreter
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws InvalidScalingFactorException 
	 * @throws TerminateException 
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return new Number(lookupCommand(operands.get(0), machine).execute(machine).doubleValue() - lookupCommand(operands.get(1), machine).execute(machine).doubleValue());
		} else if( operands.size() != maxOperands ) {
			System.err.println("error : SubtractCommand cannot proceed without exactly " + maxOperands + " operands");
			System.err.println("It now has " + operands.size() + " operands");
			System.err.println("SubtractCommand cannot return a valid value and the program must exit.");
			throw new IllegalNumberOfArgumentsException(operands.size(), "Subtract");
		}
		
		return null;
	}
}
