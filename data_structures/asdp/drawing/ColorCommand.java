package drawing;

import java.util.ArrayList;

/**A command that when invoked places the new colors (double values)
 * into the global scope, and also outputs them to PostScript
 * @author Sam Whitlock (cs61b-eo)
 */
class ColorCommand extends Command {
	
	ColorCommand() {
		maxOperands = 3;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (the red value. A value needed to be returned, and it might as well be non-null)
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if(operands.size() == maxOperands) {
				machine.drawUtil.setRGBcolor(
						lookupCommand(operands.get(0), machine).execute(machine).doubleValue(),
						lookupCommand(operands.get(1), machine).execute(machine).doubleValue(),
						lookupCommand(operands.get(2), machine).execute(machine).doubleValue()
				);
				machine.red = lookupCommand(operands.get(0), machine).execute(machine).doubleValue();
				machine.green = lookupCommand(operands.get(1), machine).execute(machine).doubleValue();
				machine.blue = lookupCommand(operands.get(2), machine).execute(machine).doubleValue();
				return lookupCommand(operands.get(0), machine).execute(machine);
			}
		
			System.err.println("error : ColorCommand cannot proceed without exactly " + maxOperands + " operands");
			System.err.println(operands.size());
			throw new IllegalNumberOfArgumentsException(operands.size(), "Color");
	}
}
