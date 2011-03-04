package drawing;

import java.util.ArrayList;

/** A command that return the multiple of its 2 operands
 * @author Sam Whitlock (cs61b-eo)
 */
class MultCommand extends Command {

	MultCommand () {
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
			return new Number(lookupCommand(operands.get(0), machine).execute(machine).doubleValue() * lookupCommand(operands.get(1), machine).execute(machine).doubleValue());
		}
		
		System.err.println("error : MultCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "Mult");
	}
}
