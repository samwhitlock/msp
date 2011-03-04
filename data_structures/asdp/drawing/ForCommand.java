package drawing;

import java.util.ArrayList;

/**A command that represents the For loop control structure
 * @author Sam Whitlock (cs61b-eo)
 */
class ForCommand extends Command {

	ForCommand() {
		minOperands = 3;
		operands = new ArrayList<Command>();
	}

	/**Special override of method in Command
	 * Executes each operand sequentially while updating a variable between 2 limits
	 * @return Value
	 * @param Interpreter
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		if( operands.size() >= minOperands ) {
			String sym;
			double begin, end;
			sym = operands.get(0).execute(machine).stringValue();
			begin = lookupCommand(operands.get(1), machine).execute(machine).doubleValue();
			end = lookupCommand(operands.get(2), machine).execute(machine).doubleValue();

			machine.setVariable(sym, new Literal(begin));
			
			while( begin <= end ) {
				for( int index = 3; index < operands.size(); index++ ) {
					lookupCommand(operands.get(index), machine).execute(machine);
				}

				machine.setVariable( sym, new Literal(++begin));
			}

			return new Number(begin);
		}

		System.err.println("error : ForCommand cannot proceed without at least " + minOperands + " operands");
		throw new IllegalNumberOfArgumentsException(operands.size(), "For");
	}
}