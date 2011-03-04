package drawing;

import java.util.ArrayList;

/** A command that return a value of the square root of its 1 operand
 * @author Sam Whitlock (cs61b-eo)
 */
class SquareRootCommand extends Command {
	
	SquareRootCommand() {
		maxOperands = 1;
		operands = new ArrayList<Command>();
	}
	
	/** Override of method in Command
	 * @param Interpreter
	 * @return Value (Number)
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws IllegalStateException 
	 * @throws InvalidScalingFactorException 
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
			return new Number( Math.sqrt( lookupCommand(operands.get(0), machine).execute(machine).doubleValue()) );
		} 
		System.err.println("error : SquareRootCommand cannot proceed without exactly " + maxOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "SquareRoot");
	}
}
