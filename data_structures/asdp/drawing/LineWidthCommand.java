package drawing;

import java.util.ArrayList;

/**A command that when executed parses its numerical operands and sets the color of the drawing functions on the PostScript output
 * @author Sam Whitlock (cs61b-eo)
 */
class LineWidthCommand extends Command {

	LineWidthCommand() {
		maxOperands = 1;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (line width)
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws InvalidScalingFactorException 
	 * @throws IllegalStateException 
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalNumberOfArgumentsException, IllegalStateException, InvalidScalingFactorException {
		if(operands.size() == maxOperands) {
			machine.drawUtil.setLineWidth(lookupCommand(operands.get(0), machine).execute(machine).doubleValue());
			machine.lineWidth = lookupCommand(operands.get(0), machine).execute(machine).doubleValue();
			return lookupCommand(operands.get(0), machine).execute(machine);
		}

		System.err.println("error : LineWidthCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "LineWidth");
	}
}
