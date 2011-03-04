package drawing;

import java.util.ArrayList;

/**A command that returns a Filled Circle by parsing its operands
 * @author Sam Whitlock (cs61b-eo)
 */
class FilledCircleCommand extends CircleCommand {

	FilledCircleCommand() {
		maxOperands = 3;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (Picture : FilledCircle)
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException  {
		if(operands.size() == maxOperands) {
			return new FilledCircle(
					lookupCommand(operands.get(0), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(1), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(2), machine).execute(machine).doubleValue(),
					machine.red, machine.green, machine.blue, machine.lineWidth
			);
		}

		System.err.println("error : CircleCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "FilledCircle");
	}
}
