package utilities;

public class CommandFactory {
	public static Command getCommand(String name, String value) {
		Command result = null;
		
		if (name.equals("i")) { // parse immediate
			result = new ParseImmediateCommand(name, value);
		} else if (name.equals("l")) { // logger
			result = new LogCommand(name, value);
		} else if (name.equals("p")) { // pagina's
			result = new PageCommand(name, value);
		} else if (name.equals("m")) { // max searches
			result = new MaxSearchCommand(name, value);
		} else if (name.equals("t")) {
			result = new TokenCommand(name, value);
		} else if (name.equals("v")) {
			result = new VisitedLinkCommand(name, value);
		}
		
		return result;
	}
}
