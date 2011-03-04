package drawing;

/**An exception to alert of an illegal number of arguments to a Command
 * @author Sam Whitlock (cs61b-eo)
 */
@SuppressWarnings("serial")
class IllegalNumberOfArgumentsException extends Exception {
	IllegalNumberOfArgumentsException() { }
	
	IllegalNumberOfArgumentsException( int badArgNum, String commandType ) {
		argNum = badArgNum;
		cmd = commandType;
	}
	
	/**Get the number of attempted arguments
	 * @param void
	 * @return int
	 */
	int getIllegalArgumentNumber() {
		return argNum;
	}
	
	public String getMessage() {
		return returnMessage;
	}
	
	/**Get the type of command that this erroneous call was issued to
	 * @param void
	 * @return String
	 */
	String getCommandType() {
		return cmd;
	}
	
	private int argNum;
	private String cmd;
	private String returnMessage = "error : Illegal number of arguments";
}
