package drawing;

import java.util.ArrayList;

/**A command that returns the rotated version of a Picture
 * @author Sam Whitlock (cs61b-eo)
 */
class RotateCommand extends Command {

	RotateCommand() {
		maxOperands = 2;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (Picture)
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
				return lookupCommand(operands.get(0), machine).execute(machine).pictureValue().rotate(
						lookupCommand(operands.get(1), machine).execute(machine).doubleValue()
						);
			}
			System.err.println("error : RotateCommand cannot proceed without exactly " + maxOperands + " operands");
			throw new IllegalNumberOfArgumentsException(operands.size(), "Rotate");
	}
}
