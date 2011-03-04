package reversigame;

/**Main class for execution
 * @author Sam Whitlock (cs61b-eo)
 */
public class Main {

	public static void main (String[] args) {
		Controller user;

		user = null;
		if (args.length == 0)
			user = new TextController ();
		else if (args.length == 1 && args[0].equals ("--display")) {
			System.err.println("error : GUI display not implemented in this version");
			System.exit(1);
		}
		else {
			System.err.println ("Usage: java reversi [--display]");
			System.exit (1);
		}
		user.process ();
	}
}