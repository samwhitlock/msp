package drawing;

/** Exception to alert of erroneous input
 * @author Sam Whitlock (cs61b-eo)
 */
@SuppressWarnings("serial")
class InvalidInputException extends Exception {
	InvalidInputException( String badInput ) {
		illegalInput = badInput;
	}
	
	/**get the invalid input
	 * @param void
	 * @return String
	 */
	String getInvalidInput() {
		return illegalInput;
	}
	
	/**get the message associated with this exception
	 * @param void
	 * @return String
	 */
	public String getMessage() {
		return returnMessage;
	}
	
	private String illegalInput;
	private String returnMessage = "";
}
