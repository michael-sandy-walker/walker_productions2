package utilities.command;

public class CommandFactory {
	
	public static final String SEPARATOR = "-#-";
	
	public static void registerCommands() {
		PageCommand.registerCommand(PageCommand.class.getSimpleName());
		MaxSearchCommand.registerCommand(MaxSearchCommand.class.getSimpleName());		
		LogCommand.registerCommand(LogCommand.class.getSimpleName());
		VisitedLinkCommand.registerCommand(VisitedLinkCommand.class.getSimpleName());
		ParseImmediateCommand.registerCommand(ParseImmediateCommand.class.getSimpleName());
		TokenCommand.registerCommand(TokenCommand.class.getSimpleName());
		ConcatenatedTokenCommand.registerCommand(ConcatenatedTokenCommand.class.getSimpleName());
		RegExCommand.registerCommand(RegExCommand.class.getSimpleName());
		CookieCommand.registerCommand(CookieCommand.class.getSimpleName());
		CategoryCommand.registerCommand(CategoryCommand.class.getSimpleName());
	}
	
	public static String getCommandParamByClassName(String className) {
		String result = null;
		if (className.equals(ParseImmediateCommand.class.getSimpleName())) {
			result = "i";
		} else if (className.equals(LogCommand.class.getSimpleName())) {
			result = "l";
		} else if (className.equals(PageCommand.class.getSimpleName())) {
			result = "p";
		}  else if (className.equals(MaxSearchCommand.class.getSimpleName())) {
			result = "m";
		} else if (className.equals(TokenCommand.class.getSimpleName())) {
			result = "t";
		} else if (className.equals(VisitedLinkCommand.class.getSimpleName())) {
			result = "v";
		} else if (className.equals(ConcatenatedTokenCommand.class.getSimpleName())) {
			result = "c";
		} else if (className.equals(RegExCommand.class.getSimpleName())) {
			result = "r";
		} else if (className.equals(CookieCommand.class.getSimpleName())) {
			result = "u";
		} else if (className.equals(CategoryCommand.class.getSimpleName())) {
			result = "k";
		} else if (className.equals(GuiCheckboxCommand.class.getSimpleName())) {
			result = "gcb";
		}
		return result;
	}
	
	public static String getCommandAbbreviationByClassName(String className) {
		String result = null;
		if (className.equals(ParseImmediateCommand.class.getSimpleName())) {
			result = "Immediate search";
		} else if (className.equals(LogCommand.class.getSimpleName())) {
			result = "Log level";
		} else if (className.equals(PageCommand.class.getSimpleName())) {
			result = "Page(s)";
		}  else if (className.equals(MaxSearchCommand.class.getSimpleName())) {
			result = "Page limit";
		} else if (className.equals(TokenCommand.class.getSimpleName())) {
			result = "Disjunction(s)";
		} else if (className.equals(VisitedLinkCommand.class.getSimpleName())) {
			result = "Link file";
		} else if (className.equals(ConcatenatedTokenCommand.class.getSimpleName())) {
			result = "Conjunction(s)";
		} else if (className.equals(RegExCommand.class.getSimpleName())) {
			result = "Regex";
		} else if (className.equals(CookieCommand.class.getSimpleName())) {
			result = "Use cookies";
		} else if (className.equals(CategoryCommand.class.getSimpleName())) {
			result = "Categories";
		} else if (className.equals(GuiCheckboxCommand.class.getSimpleName())) {
			result = "Checkbox Dummy Text";
		}
		return result;
	}
	
	public static String getClassNameByCommandParam(String commandParam) {
		String result = null;
		if (commandParam.equals("i")) {
			result = ParseImmediateCommand.class.getSimpleName();
		} else if (commandParam.equals("l")) {
			result = LogCommand.class.getSimpleName();
		} else if (commandParam.equals("p")) {
			result = PageCommand.class.getSimpleName();
		}  else if (commandParam.equals("m")) {
			result = MaxSearchCommand.class.getSimpleName();
		} else if (commandParam.equals("t")) {
//			result = "t";
			result = TokenCommand.class.getSimpleName();
		} else if (commandParam.equals("v")) {
			result = VisitedLinkCommand.class.getSimpleName();
		} else if (commandParam.equals("c")) {
			result = ConcatenatedTokenCommand.class.getSimpleName();
		} else if (commandParam.equals("r")) {
			result = RegExCommand.class.getSimpleName();
		} else if (commandParam.equals("u")) {
			result = CookieCommand.class.getSimpleName();
		} else if (commandParam.equals("k")) {
			result = CategoryCommand.class.getSimpleName();
		} else if (commandParam.equals("gcb")) {
			result = GuiCheckboxCommand.class.getSimpleName();
		}
		return result;
	}
	
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
		} else if (name.equals("c")) {
			result = new ConcatenatedTokenCommand(name, value);
		} else if (name.equals("r")) {
			result = new RegExCommand(name, value);
		} else if (name.equals("u")) {
			result = new CookieCommand(name, value);
		} else if (name.equals("k")) {
			result = new CategoryCommand(name, value);
		} else if (name.equals("gcb")) {
			result = new GuiCheckboxCommand(name, value);
		}
		
		return result;
	}
}
