package drawing;

import java.util.ArrayList;

/**A command that returns a Filled Rectangle constructed by parsing its operands
 * @author Sam Whitlock (cs61b-eo)
 */
class FilledRectangleCommand extends RectangleCommand {

	FilledRectangleCommand() {
		maxOperands = 4;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (FilledRectangle)
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return new FilledRectangle(
					lookupCommand(operands.get(0), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(1), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(2), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(3), machine).execute(machine).doubleValue(),
					0, machine.red, machine.green, machine.blue, machine.lineWidth
			);
		}

		System.err.println("error : FilledRectangleCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "FilledRectangle");
	}

}
