package utilities.command;

public class ParseImmediateCommand extends Command {
	
	private static boolean parseImmediate = false;
	
	public ParseImmediateCommand(String name, String value) {
		super(name, value);
		if (value != null) {
			parseImmediate = value.toLowerCase().equals("y");
		} else {
			parseImmediate = true;
		}
	}
	
	public static boolean isParseImmediate() {
		return parseImmediate;
	}

}
