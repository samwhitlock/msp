package drawing;

/**An exception that signals that a command was not defined
 * @author Sam Whitlock (cs61b-eo)
 */
@SuppressWarnings("serial")
class CommandNotDefinedException extends Exception {
	CommandNotDefinedException( String badCmd ) {
		cmd = badCmd;
	}
	
	/**Return the String representation of the bad command
	 * @param void
	 * @return String
	 */
	String getUndefinedCommand() {
		return cmd;
	}
	
	public String getMessage() {
		return returnMessage;
	}
	
	private String cmd;
	private String returnMessage = "error : Command not defined in this interpreter";
}
