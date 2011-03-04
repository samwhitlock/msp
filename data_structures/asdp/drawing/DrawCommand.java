package drawing;

import java.util.ArrayList;
/**A command that will draw its call the draw method on its single Picture value operand (may be a picture or a group)
 * @author Sam Whitlock (cs61b-eo)
 */
class DrawCommand extends Command {

	DrawCommand() {
		maxOperands = 1;
		operands = new ArrayList<Command>();
	}

	/**Override of method in Command
	 * @param Interpreter
	 * @return Value of type Picture
	 * @throws InvalidScalingFactorException 
	 * @throws IllegalNumberOfArgumentsException 
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			lookupCommand(operands.get(0), machine).execute(machine).pictureValue().draw(machine.drawUtil);
			return lookupCommand(operands.get(0), machine).execute(machine);
		}

		System.err.println("DrawCommand cannot return a valid value and the program must exit.");
		throw new IllegalNumberOfArgumentsException(operands.size(), "Draw");
	}
}
