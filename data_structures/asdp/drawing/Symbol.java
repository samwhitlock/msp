package drawing;

/**Symbol extends Value and is a wrapper for a String. It is used to represent symbols
 * @author Sam Whitlock (cs61b-eo)
 */
class Symbol extends Value {
	private String sym;
	
	Symbol( String str ) {
		sym = str;
	}
	
	/**Override of stringValue() from Value
	 * @return String
	 * @param void
	 */
	@Override
	 String stringValue() {
    	return sym;
    }
}
