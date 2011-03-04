package drawing;

import java.util.ArrayList;
/**A command that represents the addition of 2 numbers
 * @author Sam Whitlock (cs61b-eo)
 */
class AddCommand extends Command {

	AddCommand() {
		maxOperands = 2;
		operands = new ArrayList<Command>();
	}

	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException  {
		if( operands.size() == maxOperands ) {
			return new Number(lookupCommand(operands.get(0), machine).execute(machine).doubleValue() + lookupCommand(operands.get(1), machine).execute(machine).doubleValue());
		}

		System.err.println("error : AddCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "Add");
	}
}
