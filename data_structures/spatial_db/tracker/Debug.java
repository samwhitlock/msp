package tracker;

/**A class the handle the printing of error messages and setting of exit statuses
 * No method has documentation because their names are all self-explanatory
 * @author Sam Whitlock (cs61b-eo)
 */
class Debug {
	static int debugOption;
	static {
		debugOption = 0;
	}
	
	static void cannotWriteToFile(String badFileName) {
		if( debugOption > 0) {
		System.err.println("error : Cannot write to file");
		System.err.println("file name: " + badFileName);
		}
		Main.systemExitStatus = 1;
	}
	
	static void cannotReadFile(String badFileName) {
		if( debugOption > 0) {
		System.err.println("error : Cannot read from file");
		System.err.println("file name: " + badFileName);
		}
		Main.systemExitStatus = 1;
	}
	
	static void idAlreadyPresent(int ID) {
		if( debugOption > 0) {
		System.err.println("error : id already present in tree: " + ID);
		}
		Main.systemExitStatus = 1;
	}
	
	static void help() {
		System.out.println("Please see User-Manual for a listing of commands and debug options");
	}
	
	static void cannotShrinkBounds() {
		if( debugOption > 0) {
		System.err.println("error : cannot shrink bounds of tree");
		}
		Main.systemExitStatus = 1;
	}
	
	static void cannotIncreaseRadius( double oldRad, double newBadRad ) {
		if( debugOption > 0) {
		System.err.println("error : cannot increase radius of objects");
		}
		Main.systemExitStatus = 1;
	}
	
	static void negativeID(int badID) {
		if( debugOption > 0) {
		System.err.println("error : ID of point cannot be negative");
		}
		Main.systemExitStatus = 1;
	}
	
	static void badArgNum( int badArgNum, int goodArgNum ) {
		if( debugOption > 0) {
		System.err.println("error : bad argument number to command");
		System.err.println("command requires " + goodArgNum + " commands");
		System.err.println("you issued " + badArgNum + " commands");
		}
		Main.systemExitStatus = 1;
	}
	
	static void badNumberFormat(String badNum) {
		if( debugOption > 0) {
		System.err.println("error : could not format number from string");
		System.err.println("bad number string: " + badNum);
		}
		Main.systemExitStatus = 1;
	}
	
	static void badNumberFormat(String[] str) {
		if( debugOption > 0) {
		System.err.println("Bad number format : ");
		for( String s : str ) {
			System.err.println(s);
		}
		}
		Main.systemExitStatus = 1;
	}
	
	static void badCommand(String str) {
		if( debugOption > 0) {
		System.err.println("error : bad command issued : " + str);
		}
		Main.systemExitStatus = 1;
	}

	public static void unexpectedEndOfInput() {
		if( debugOption > 0) {
		System.err.println("Unexpected end of input. There is no more input and the quit command hasn't been issued");
		}
		Main.systemExitStatus = 1; Main.exit();
	}
}
