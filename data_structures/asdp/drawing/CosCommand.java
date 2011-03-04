package drawing;

import java.util.ArrayList;

/** Command that, when executed will return the value of the cosine of its operand in degrees
 * @author Sam Whitlock (cs61b-eo)
 */
class CosCommand extends Command {
	
	CosCommand() {
		maxOperands = 1;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in command
	 * @param Interpreter
	 * @return void
	 * @throws InvalidScalingFactorException 
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws IllegalStateException 
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return new Number( Math.cos( Math.toRadians( lookupCommand(operands.get(0), machine).execute(machine).doubleValue() ) ) );
		}

		System.err.println("error : CosCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "Cos");
	}
}
