package drawing;

import java.util.ArrayList;

/** A command that return the sin (in degrees) of its (with degrees in radians)
 * @author Sam Whitlock (cs61b-eo)
 */
class SinCommand extends Command {

	SinCommand() {
		maxOperands = 1;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (Number)
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws IllegalStateException 
	 * @throws InvalidScalingFactorException 
	 * @throws TerminateException 
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return new Number( Math.sin( Math.toRadians( lookupCommand(operands.get(0), machine).execute(machine).doubleValue() ) ) );
		}
			System.err.println("error : SinCommand cannot proceed without exactly " + maxOperands + " operands");
			throw new IllegalNumberOfArgumentsException(operands.size(), "Sin");
	}
}
