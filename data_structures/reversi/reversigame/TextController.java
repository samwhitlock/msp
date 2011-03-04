package reversigame;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

/** A Controller whose input source is the standard input.
 * @author Sam Whitlock (cs61b-eo)
 * */
class TextController extends Controller {
	private final Stack<BufferedReader> inputs = new Stack<BufferedReader> ();

	TextController () {
		inputs.push (new BufferedReader (new InputStreamReader (System.in)));
	}

	void prepend (BufferedReader stream) {
		inputs.push (stream);
	}

	/** Read a line of input from System.in, removing comments and 
	 *  leading and trailing whitespace, and skipping blank lines.  
	 *  Returns null when input exhausted.
	 *  @author Sam Whitlock (cs61b-eo)
	 *  */
	String getLine () {
		try {
			String line;
			do {
				if (inputs.empty ())
					return null;
				if(inputs.size() == 1) {
					System.out.print("> ");
				}
				line = inputs.peek().readLine ();
				if (line == null) {
					inputs.pop ().close ();
					line = "";
					continue;
				}
				if (line.indexOf ('#') != -1)
					line = line.substring (0, line.indexOf ('#'));
				line = line.trim ();
			} while (line.length () == 0);
			return line;
		} catch (IOException e) {
			return null;
		}
	}

}
