package drawing;

import java.util.ArrayList;

/**A command that when executed returns a Rectangle by parsing its operands
 * @author Sam Whitlock (cs61b-eo)
 */
class RectangleCommand extends Command {

	RectangleCommand() {
		maxOperands = 4;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (Rectangle)
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws InvalidScalingFactorException 
	 * @throws IllegalStateException 
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalNumberOfArgumentsException, IllegalStateException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
				return new Rectangle(
						lookupCommand(operands.get(0), machine).execute(machine).doubleValue(),
						lookupCommand(operands.get(1), machine).execute(machine).doubleValue(),
						lookupCommand(operands.get(2), machine).execute(machine).doubleValue(),
						lookupCommand(operands.get(3), machine).execute(machine).doubleValue(),
						0, machine.red, machine.green, machine.blue, machine.lineWidth
				);
			}
			System.err.println("error : RectangleCommand cannot proceed without exactly " + maxOperands + " operands");
			throw new IllegalNumberOfArgumentsException(operands.size(), "Rectangle");
		}
}
