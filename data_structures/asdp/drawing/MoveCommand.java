package drawing;

import java.util.ArrayList;

/**A command that returns the translation of a Picture
 * @author Sam Whitlock (cs61b-eo)
 */
class MoveCommand extends Command {

	MoveCommand() {
		maxOperands = 3;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (Number)
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalNumberOfArgumentsException, IllegalStateException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return lookupCommand(operands.get(0), machine).execute(machine).pictureValue().translate(
					lookupCommand(operands.get(1), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(2), machine).execute(machine).doubleValue()
			);
		}

		System.err.println("error : MoveCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "Move");
	}
}
