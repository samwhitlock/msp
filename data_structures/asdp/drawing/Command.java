package drawing;

import java.util.List;

/** The superclass of all types of Command. A Command is described by
 *  a non-numeric S-expression, as indicated in the specifications.
 *  @author Sam Whitlock (cs61b-eo)
 */
abstract class Command {
	
	void addCommand(Command cmd) {
			operands.add(cmd);
	}
	
	Command lookupCommand(Command cmd, Interpreter machine) {
		if( cmd instanceof SymbolCommand ) {
			try {
				SymbolCommand coercedCmd = (SymbolCommand) cmd;
				return machine.getVariable(coercedCmd.execute(machine).stringValue());
			} catch (VariableNotFoundException e) {
				System.err.println(e.getMessage());
				System.err.println("Variable " + e.getUndefinedVariable() + " is not defined at time of invocation");
				System.err.println("System cannot proceed because the variable needs to be associated with a value at this time.");
				System.exit(1);
			}
		}
		return cmd;
	}
    
    /** Execute me, modifying the state in MACHINE as appropriate and
     *  returning the resulting Value.
     *  @param Interpreter
     *  @return Value
     * @throws TerminateException 
     * @throws IllegalNumberOfArgumentsException 
     * @throws IllegalStateException 
     * @throws InvalidScalingFactorException 
     *  */
    abstract Value execute (Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException;
    
    protected List<Command> operands;
    protected int maxOperands, minOperands;
}

