package drawing;

import java.util.ArrayList;

/**A command that represents an assignment of a Value (Command) to a Variable (Symbol wraps a String)
 * @author Sam Whitlock (cs61b-eo)
 */
class AssignCommand extends Command {
	
	AssignCommand() {
		maxOperands = 2;
		operands = new ArrayList<Command>();
	}
	/**Special override of method in Command
	 * @param Interpreter
	 * @return Value of assignment (what the value assigned to the variable was)
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() == maxOperands ) {
				machine.setVariable(operands.get(0).execute(machine).stringValue(), lookupCommand(operands.get(1), machine));
				return operands.get(1).execute(machine);
			}
		
			System.err.println("error : AssignCommand cannot proceed without exactly " + maxOperands + " operands");
			throw new IllegalNumberOfArgumentsException(operands.size(), "Assign");
	}
}
