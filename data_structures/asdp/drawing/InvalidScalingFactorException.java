package drawing;

/**An exception to alert of an invalid scaling factor in the Picture values
 * @author Sam Whitlock (cs61b-eo)
 */
@SuppressWarnings("serial")
class InvalidScalingFactorException extends Exception {
	InvalidScalingFactorException( double factor ) {
		invalidScalingFactor = factor;
	}
	
	/**get the message associated with this exception
	 * @return String
	 * @param void
	 */
	public String getMessage() {
		return returnMessage;
	}
	
	/** get the invalid scaling factor associated with this exception
	 * @return double
	 * @param void
	 */
	double getInvalidScalingFactor() {
		return invalidScalingFactor;
	}
	
	private double invalidScalingFactor;
	private String returnMessage = "error : Invalid scaling factor; All scaling factor must be positive";
}
