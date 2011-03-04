package reversigame;

/** All things to do with parsing commands.
 * @author Sam Whitlock (cs61b-eo)
 */
enum Command {

	/** Command types.  PIECEMOVE indicates a move of the form 
	 *  cr.  ERROR indicates a parse error in the command.  
	 *  All other commands are upper-case versions of what the
	 *  programmer writes. */
	ERROR, 
	AUTO, CLEAR, MANUAL, COLOR, HOST, JOIN, SEED, START,
	PASS, PIECEMOVE, MOVES, 
	RESIGN,
	LOAD, HELP, DUMP, QUIT, PRINT;


	/** Parse COMMAND, returning the type of command.  If COMMAND
	 *  takes an operand, returns it in OPND[0]. COMMAND should not 
	 *  contain leading or trailing whitespace.  If 
	 *  COMMAND is an illegal command, returns ERROR. */
	static Command parseCommand (String cmmand, String[] opnd) {
		String command = cmmand.trim();
		if( command.compareToIgnoreCase("resign") == 0 ) {
			return RESIGN;
		} else if( command.compareToIgnoreCase("moves") == 0 ) {
			return MOVES;
		} else if( command.compareToIgnoreCase("seed") == 0 ) {
			return SEED;
		} else if( command.compareToIgnoreCase("quit") == 0 ) {
			return QUIT;
		} else if( command.compareToIgnoreCase("dump") == 0 ) {
			return DUMP;
		} else if( command.compareToIgnoreCase("auto") == 0 ) {
			return AUTO;
		} else if( command.compareToIgnoreCase("clear") == 0 ) {
			return CLEAR;
		} else if( command.compareToIgnoreCase("manual") == 0 ) {
			return MANUAL;
		} else if( command.compareToIgnoreCase("start") == 0 ) {
			return START;
		} else if( command.compareToIgnoreCase("help") == 0 ) {
			return HELP;
		} else if( command.compareTo("-") == 0 ) {
			return PASS;
		} else if( command.compareToIgnoreCase("print") == 0 ) {
			return PRINT;
		} else if( command.matches("([cC][oO][lL][oO][rR])\\s+(([bB][lL][aA][cC][kK])|([wW][hH][iI][tT][eE]))") ) {
			String[] parsed = command.split("\\s+");
			if( parsed.length == 2 ) {
				opnd[0] = parsed[1];
				return COLOR;
			} else return ERROR;
		} else if( command.matches("[a-hA-H][1-8]") ) {
			opnd[0] = command;
			return PIECEMOVE;
		} else if( command.startsWith("load") ) {
			String[] parsed = command.split("\\s+");
			if( parsed.length == 2 ) {
				opnd[0] = parsed[1];
				return LOAD;
			} else return ERROR;
		} else if( command.matches("[sS][eE]{2}[dD]\\s+\\d+") ) {
			return SEED;
		} else if( command.matches("[hH][oO][sS][tT]\\s+\\w+") ) {
			String[] parsed = command.split( "\\s+" );
			if( parsed.length == 2 ) {
				opnd[0] = parsed[1];
				return HOST;
			} else return ERROR;
		} else if( command.startsWith("join") ) {
			String[] parsed = command.split("\\s+");
			if( parsed.length == 2 ) {
				opnd[0] = idPart(parsed[1]);
				opnd[1] = hostPart(parsed[1]);
				return JOIN;
			} else return ERROR;
		}
		else return ERROR;
	}

	/** The ID part of a 'join' operand.  Returns null for an invalid
	 *  'join' operand. */
	static String idPart (String opnd) {
		String[] x = opnd.split("@");
		if( x.length == 2 ) {
			return x[0];
		} else return opnd;
	}

	/** The HOST part of a 'join' operand.  Returns null for an invalid
	 *  'join' operand. */
	static String hostPart (String opnd) {
		String[] x = opnd.split("@");
		if( x.length == 2 ) {
			return x[1];
		} else return opnd;
	}
}

