package tracker;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import java.util.Stack;

/**The main class in the tracker package
 * @author Sam Whitlock (cs61b-eo)
 */
public class Main {
	static int systemExitStatus;
	static boolean printPrompt;
	static Stack<Scanner> inputs;
	
	static {
		systemExitStatus = 0;
		inputs = new Stack<Scanner>();
	}

	public static void main (String... args) {
		String inputFileName = null, outputFileName = null;

		if (args.length == 1) {
			if ( args[0].startsWith("--debug=") ) {
				setupDebugOption(args[0]);
			} else {
				inputFileName = args[0];
			}
		} else if(args.length == 2) {
			if ( args[0].startsWith("--debug=") ) {
				setupDebugOption(args[0]);
				inputFileName = args[1];
			} else {
				inputFileName = args[0];
				outputFileName = args[1];
			}
		} else if(args.length == 3) {
			setupDebugOption(args[0]);
			inputFileName = args[1];
			outputFileName = args[2];
		} else if(args.length > 3){
			System.err.println("Invalid number of arguments to the Particle Tracker Program");
			System.err.println("Please refer to the UserManual for more information about valid parameters");
			System.err.println("for this program");
			System.exit(1);
		}

		if (inputFileName == null) {
			inputs.push (new Scanner (System.in));
			printPrompt = true;
		} else {
			try {
				inputs.push (new Scanner (new File (inputFileName)));
			} catch (FileNotFoundException e) {
				System.err.printf ("Error: could not open %s%n", inputFileName);
				System.exit (1);
			}

			if (outputFileName != null) {
				try {
					System.setOut (new PrintStream (outputFileName));
				} catch (FileNotFoundException e) {
					System.err.printf ("Error: could not open %s%n", 
							outputFileName);
					System.exit (1);
				}
			}
		}
		
		System.out.println ("Particle tracker, v5.0.");
		
		while (! inputs.empty ()) {
			if (printPrompt) {
				System.out.print("> ");
			}
			
			if (!inputs.peek ().hasNext ()) {
				inputs.pop();
				if(inputs.size() == 1 && inputFileName == null) {
					printPrompt = true;
				}
				continue;
			}

			Parser.parseLine(inputs.peek());
		}
		
		Debug.unexpectedEndOfInput();
	}

	/**Sets the debug option in both the tracker package and 
	 * sets it in the Util package through its public interface
	 * @author Sam Whitlock (cs61b-eo)
	 * @param numStr
	 */
	private static void setupDebugOption( String numStr ) {
		try {
			int temp = Integer.parseInt( numStr.substring(8) );
			if ( temp == 0 || temp == 1 ) {
				Debug.debugOption = temp;
				util.Debugging.setDebuggingLevel(temp);
			} else {
				System.err.println("Invalid debug option: " + numStr);
				System.err.println("Please refer to the UserManual or INTERALS");
				System.err.println("for more information about debug modes/options");
				System.exit(1);
			}
		} catch (NumberFormatException e) {
			System.err.println("Invalid debug option: " + numStr);
			System.err.println("Please refer to the UserManual or INTERALS");
			System.err.println("for more information about debug modes/options");
			System.exit(1);
		}
	}

	/**Closes the current output stream and
	 * exits the program with any saved exit status
	 * @author Sam Whitlock (cs61b-eo)
	 */
	static void exit() {
		System.out.close();
		System.exit(systemExitStatus);
	}
}
