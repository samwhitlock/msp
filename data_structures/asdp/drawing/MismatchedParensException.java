package drawing;

/**An exception to alert when there is an unmatched parenthesis
 * @author Sam Whitlock (cs61b-eo)
 */
@SuppressWarnings("serial")
class MismatchedParensException extends Exception {
	MismatchedParensException() { }
	
	/**Return the message associated with this exception
	 * @param void
	 * @return String
	 */
	public String getMessage() {
		return errorMessage;
	}
	
	private String errorMessage = "error : Illegal location of closed parenthesis";
}
