package drawing;
/** An Exception for when variables (Symbols) aren't found at the global scope
 * @author Sam Whitlock (cs61b-eo)
 */
@SuppressWarnings("serial")
class VariableNotFoundException extends Exception {
	VariableNotFoundException( String badVar ) {
		undefinedVariable = badVar;
	}
	
	VariableNotFoundException( Symbol sym ) {
		undefinedVariable = sym.stringValue();
	}
	/**Return the undefined variable
	 * @param void
	 * @return String
	 */
	String getUndefinedVariable() {
		return undefinedVariable;
	}
	
	/** Return the message associated with this exception
	 * @param void
	 * @return String
	 */
	public String getMessage() {
		return returnMessage;
	}
	
	private String undefinedVariable;
	private String returnMessage = "error : Variable not defined in scope";
}
