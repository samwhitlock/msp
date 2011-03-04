package drawing;

import java.util.ArrayList;
/**A command that returns the scaled version of a picture
 * @author Sam Whitlock (cs61b-eo)
 */
class ScaleCommand extends Command {
	
	ScaleCommand() {
		maxOperands = 2;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return lookupCommand(operands.get(0), machine).execute(machine).pictureValue().scale(
					lookupCommand(operands.get(1), machine).execute(machine).doubleValue()
			);
		}
		System.err.println("error : AssignCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "Scale");

	}
}
