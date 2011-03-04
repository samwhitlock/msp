package drawing;

import java.util.ArrayList;

/**A command that will return a Group
 * @author Sam Whitlock (cs61b-eo)
 */
class GroupCommand extends Command {

	GroupCommand() {
		maxOperands = -1;
		operands = new ArrayList<Command>();
	}
	
	/**Override of method in Command
	 * @param Interpreter
	 * @return Value (Group)
	 */
	@Override
	Value execute(Interpreter machine) throws IllegalStateException, IllegalNumberOfArgumentsException, InvalidScalingFactorException {
		ArrayList<Picture> retList = new ArrayList<Picture>();
		if( operands.isEmpty() ) {
			return new Group();
		} else {
			for( Command c : operands ) {
				retList.add(lookupCommand(c, machine).execute(machine).pictureValue());
			}
		}
		return new Group(retList);
	}
}
