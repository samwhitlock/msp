package drawing;

import java.util.ArrayList;

/**A command that when executed produces a line by parsing the its operands
 * @author Sam Whitlock (cs61b-eo)
  */
class LineCommand extends Command {

	LineCommand() {
		maxOperands = 4;
		operands = new ArrayList<Command>();
	}

	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (Picture: Line)
	 * @throws InvalidScalingFactorException 
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws IllegalStateException 
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return new Line(
					lookupCommand(operands.get(0), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(1), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(2), machine).execute(machine).doubleValue(),
					lookupCommand(operands.get(3), machine).execute(machine).doubleValue(),
					machine.red, machine.green, machine.blue, machine.lineWidth
			);
		}

		System.err.println("error : LineCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "Line");
	}
}
