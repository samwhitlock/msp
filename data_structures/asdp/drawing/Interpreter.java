package drawing;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/** An Interpreter maintains all necessary information about the running
 *  program, such as the values of variables and the destination of output.
 *  @author Sam Whitlock (cs61b-eo)
 */
class Interpreter {
	/** A HashMap implementation for variable at the global (only) level
	 */
	HashMap<String, Command> varTable = new HashMap<String, Command>();
	DrawingUtility drawUtil;

	/** The initial pattern for readCommand. This is executed to check for either the base
	 * cases (number or symbol) or for an operator.
	 */
	final static Pattern tokenPattern = Pattern.compile(
			"(([-\\+]?((\\d+\\.?\\d*)|(\\.\\d+))(e[-\\+]?\\d+)?)(\\.\\d*)?)"
			+ "|" +
			"([^\\d\\(\\)\\;\\s][\\S&&[^\\(\\)\\;]]*)"
			+ "|" +
			"(\\()"
			+ "|" +
			"(\\))"
			+ "|" +
			"(;).*"
	);
	
	final static Pattern breakPattern = Pattern.compile( "(\\)).*" );
	final static Pattern openParenPattern = Pattern.compile("(\\().*");
	final static Pattern symbolPattern = Pattern.compile("([^\\d\\(\\)\\;\\s][\\S&&[^\\(\\)\\;]]*)");

	PrintWriter output;
	Scanner inp;
	
	double red = 0, green = 0, blue = 0, lineWidth = 1;
	
	public Interpreter(Scanner in, PrintWriter out) {
		drawUtil = new DrawingUtility(out);
		output = out;
		inp = in;
	}
	
	void readExecute() {
	 try {
     	readAndExecute ();
     } catch (CommandNotDefinedException e) {
     	System.err.println(e.getMessage());
     	System.err.println("Undefined command: " + e.getUndefinedCommand());
     	System.err.print("Parsing and execution cannot proceed with an illegal command. ");
     	System.err.println("Program will exit with error status");
     	System.exit(1);
     } catch (IllegalStateException e) {
    	 System.err.println(e.getMessage() );
    	 System.exit(1);
     } catch (IllegalNumberOfArgumentsException e) {
    	 System.err.println( e.getMessage() );
    	 System.exit(1);
     } catch (InvalidScalingFactorException e) {
    	 System.err.println(e.getMessage() );
    	 System.exit(1);
     } catch (InvalidInputException e) {
    	 System.err.println(e.getMessage());
    	 System.exit(1);
     } catch (MismatchedParensException e) {
    	 System.err.println(e.getMessage());
    	 System.exit(1);
     } catch (VariableNotFoundException e) {
    	 System.err.println(e.getMessage());
    	 System.exit(1);
     }
	}

	/** Look up a variable and return its value. If the variable does not exist, a variable-not-found exception will be thrown.
	 * @param Variable Name (type: String)
	 * @return Variable Value (type: Value)
	 * @throws InvalidScalingFactorException 
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws IllegalStateException 
	 * @throws Variable Not Found exception
	 */
	Value getVariableValue(String variableName) throws VariableNotFoundException, IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		return getVariable( variableName ).execute(this);
	}

	/** Look up a variable and return its value. If the variable does not exist, a variable-not-found exception will be thrown.
	 * @param Variable Name (type: Symbol)
	 * @return Variable Value (type: Value)
	 * @throws InvalidScalingFactorException 
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws IllegalStateException 
	 * @throws Variable Not Found exception
	 */
	Value getVariableValue(Symbol sym) throws VariableNotFoundException, IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		return getVariable(sym.stringValue()).execute(this);
	}

	/** Look up a variable and return its command representation. If the variable does not exist, a variable-not-found exception will be thrown.
	 * @param Variable Name (type: String)
	 * @return Variable Value (type: Command)
	 * @throws Variable Not Found exception
	 */
	Command getVariable (String variableName) throws VariableNotFoundException {
		if( varTable.get(variableName) != null ) {
			return varTable.get(variableName);
		} else {
			throw new VariableNotFoundException(variableName);
		}
	}

	/** Look up a variable and return its command representation. If the variable does not exist, a variable-not-found exception will be thrown.
	 * @param Variable Name (type: Symbol)
	 * @return Variable Value (type: Command)
	 * @throws Variable Not Found exception
	 */
	Command getVariable (Symbol sym) throws VariableNotFoundException {
		return getVariable(sym.stringValue());
	}



	/** Set a variable.
	 * @param Variable Name (type: String), Variable Value (type: Command)
	 * @return void
	 * */
	void setVariable( String variableName, Command variableValue ) {
		varTable.put(variableName, variableValue);
	}

	/** Set a variable.
	 * @param Variable Name (type: Symbol), Variable Value (type: Command)
	 * @return void
	 */
	void setVariable( Symbol sym, Command variableValue ) {
		varTable.put(sym.stringValue(), variableValue);
	}

	/** Read a drawing program from INP and execute it.
	 * @param void
	 * @return void
	 * @throws CommandNotDefinedException
	 * @throws InvalidScalingFactorException 
	 * @throws IllegalNumberOfArgumentsException 
	 * @throws IllegalStateException 
	 * @throws VariableNotFoundException 
	 * @throws MismatchedParensException 
	 * @throws InvalidInputException 
	 * @throws TerminateException 
	 */
	void readAndExecute () throws CommandNotDefinedException, IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException, InvalidInputException, MismatchedParensException, VariableNotFoundException {
		Command executable = null;
		while( inp.hasNext() ) {
				executable = readCommand(inp);
				executable.execute(this);
		}
	}


	/** Read and return the next command from input, without executing it. 
	 * @param Scanner
	 * @return Command
	 * @throws CommandNotDefinedException 
	 * @throws EndSuccessException 
	 * @throws CommandNotDefinedException */
	private Command readCommand (Scanner input) throws InvalidInputException, MismatchedParensException, VariableNotFoundException, CommandNotDefinedException {
		if(input.hasNext()) {
			input.findWithinHorizon(tokenPattern, 0);

			if( input.match().group(2) != null ) {
				try {
					return new Literal(Double.parseDouble(input.match().group(2)));
				} catch (NumberFormatException e) {
					throw new InvalidInputException( input.match().group(1));
				}
			} else if( input.match().group(8) != null ) {
				try {
					return getVariable(input.match().group(8));
				} catch (VariableNotFoundException e) {
					return new SymbolCommand(input.match().group(8));
				}

			} else if( input.match().group(9) != null ) {
				Command retCommand = lookupCommand(input);

				while(!input.hasNext(breakPattern)) {
					if(input.hasNext(openParenPattern)) {
						retCommand.addCommand(readCommand(input));
					} else {
						input.findWithinHorizon(tokenPattern, 0);
						if( input.match().group(2) != null) {
							retCommand.addCommand(new Literal(Double.parseDouble(input.match().group(2))));
						} else if( input.match().group(8) != null) {
								retCommand.addCommand( new SymbolCommand(input.match().group(8)) );
						} else if( input.match().group(11) != null ) {
							input.nextLine();
						} else {
							throw new InvalidInputException(input.match().group(0) );
						}

					}
				}

				input.findWithinHorizon(tokenPattern, 0);

				return retCommand;
			} else if( input.match().group(10) != null ) {
				throw new MismatchedParensException();
			}  else if( input.match().group(11) != null ) {
				input.nextLine();
				return readCommand(input);
			} else {
				throw new InvalidInputException( input.match().group(0) );
			}
		}
		
		throw new IllegalStateException();
	}

	/** Takes scanner a searches for the first token (which has to be a string) and returns a command
	 * based on that
	 * @param Scanner
	 * @return Command
	 * @throws CommandNotDefinedException
	 */
	private Command lookupCommand(Scanner input) throws CommandNotDefinedException {
		String operatorType;
		do {
			input.findWithinHorizon(tokenPattern, 0);
			if( input.match().group(11) != null ) {
				input.nextLine();
			} else if( input.match().group(8) != null ) {
				operatorType = input.match().group(8);
				break;
			} else {
				throw new CommandNotDefinedException(input.match().group());
			}
		} while(true);
		if( !operatorType.isEmpty() ) {
			if( operatorType.compareTo(":=") == 0 ) {
				return new AssignCommand();
			} else if (operatorType.compareTo("+") == 0) {
				return new AddCommand();
			} else if (operatorType.compareTo("-") == 0) {
				return new SubtractCommand();
			} else if (operatorType.compareTo("*") == 0) {
				return new MultCommand();
			} else if (operatorType.compareTo("/") == 0) {
				return new DivideCommand();
			} else if (operatorType.compareTo("sin") == 0) {
				return new SinCommand();
			} else if (operatorType.compareTo("cos") == 0) {
				return new CosCommand();
			} else if (operatorType.compareTo("sqrt") == 0) {
				return new SquareRootCommand();
			} else if (operatorType.compareTo("rect") == 0) {
				return new RectangleCommand();
			} else if (operatorType.compareTo("filledrect") == 0) {
				return new FilledRectangleCommand();
			} else if (operatorType.compareTo("circ") == 0) {
				return new CircleCommand();
			} else if (operatorType.compareTo("filledcirc") == 0) {
				return new FilledCircleCommand();
			} else if (operatorType.compareTo("line") == 0) {
				return new LineCommand();
			} else if (operatorType.compareTo("group") == 0) {
				return new GroupCommand();
			} else if (operatorType.compareTo("color") == 0) {
				return new ColorCommand();
			} else if (operatorType.compareTo("linewidth") == 0) {
				return new LineWidthCommand();
			} else if (operatorType.compareTo("move") == 0) {
				return new MoveCommand();
			} else if (operatorType.compareTo("rotate") == 0) {
				return new RotateCommand();
			} else if (operatorType.compareTo("scale") == 0) {
				return new ScaleCommand();
			} else if (operatorType.compareTo("draw") == 0) {
				return new DrawCommand();
			} else if (operatorType.compareTo("for") == 0) {
				return new ForCommand();
			} 
		}

		throw new CommandNotDefinedException( operatorType );
	}
}


