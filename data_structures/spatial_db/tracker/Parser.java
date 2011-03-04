package tracker;

import java.util.ArrayList;
import java.util.Scanner;

/** An interpreter that handles the parsing and execution of commands
 * @author Sam Whitlock (cs61b-eo)
 */
class Parser {
	
	/**Parses a given line based on the specifications of the program and
	 * sends it to the process methods to determine which method in Command
	 * to send it to
	 * @author Sam Whitlock (cs61b-eo)
	 * @param input
	 */
	static void parseLine(Scanner input) {
		if( input.hasNext() ) {
			String[]  uncommented = input.nextLine().split("\\s*#.*");
			if( uncommented.length > 0 ) {
				String[] commands = uncommented[0].split("\\s*;\\s*");
				ArrayList<String[]> cmds = new ArrayList<String[]>();
				for( String s : commands ) {
					cmds.add(s.trim().split("\\s+"));
				}

				if( !cmds.isEmpty() ) {
					for( String[] x : cmds ) {
						process(x);
					}
				}
			}
		}
	}
	
	/**Takes the parsed string array from parseLine and sends it
	 * to the appropriate method in Command
	 * @author Sam Whitlock (cs61b-eo)
	 * @param cmd
	 */
	private static void process(String[] cmd) {
		if( cmd.length > 0) {
			if( "add".startsWith(cmd[0]) ) {
				Command.add(cmd);
			} else if ("bounds".startsWith(cmd[0]) ) {
				Command.bounds(cmd);
			} else if ("rad".startsWith(cmd[0])) {
				Command.rad(cmd);
			} else if ("load".startsWith(cmd[0])) {
				Command.load(cmd);
			} else if ("write".startsWith(cmd[0])) {
				Command.write(cmd);
			} else if ("near".startsWith(cmd[0])) {
				Command.near(cmd);
			} else if ("closer-than".startsWith(cmd[0])) {
				Command.closerThan(cmd);
			} else if ("simulate".startsWith(cmd[0])) {
				Command.simulate(cmd);
			} else if ("help".startsWith(cmd[0])) {
				Command.help(cmd);
			} else if ("quit".startsWith(cmd[0])) {
				Command.quit(cmd);
			} else {
				Debug.badCommand(cmd[0]);
			}
		}
	}
}