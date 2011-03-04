package drawing;
/** A wrapper class for Symbols. These are used to represent Symbols in operand sequences, which
 * may get replaced by a Value is the Symbol is looked up
 * @author Sam Whitlock (cs61b-eo)
 */
class SymbolCommand extends Command {

	private Symbol sym;
	
	SymbolCommand(String str) {
		sym = new Symbol(str);
	}
	
	SymbolCommand(Symbol symb) {
		sym = symb;
	}
	/** Override of method in Command
	 * @return Value
	 * @param Interpreter
	 */
	@Override
	Value execute(Interpreter machine) {
		return sym;
	}
}
