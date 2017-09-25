package utilities.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.tuple.Pair;

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
		} else if (className.equals(NumbExConditionalCommand.class.getSimpleName())) {
			result = "nc";
		} else if (className.equals(NumbExMathematicalCommand.class.getSimpleName())) {
			result = "nm";
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
		} else if (className.equals(NumbExConditionalCommand.class.getSimpleName())) {
			result = "NumbexC";
		} else if (className.equals(NumbExMathematicalCommand.class.getSimpleName())) {
			result = "NumbexM";
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
		} else if (commandParam.equals("nc")) {
			result = NumbExConditionalCommand.class.getSimpleName();
		} else if (commandParam.equals("nm")) {
			result = NumbExMathematicalCommand.class.getSimpleName();
		}
		return result;
	}
	
	public static Command getCommand(String name, String value) {
		Command result = null;
		
		int indexOfSubCommand = value.indexOf("--");
		String subCommandLine = null;
		if (indexOfSubCommand >= 0) {
			subCommandLine = value.substring(indexOfSubCommand);
			int endSubCommand = subCommandLine.indexOf(Command.DELIMITER);
			if (endSubCommand >= 0)
				subCommandLine = subCommandLine.substring(0, endSubCommand);
		}
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
			result = new RegExCommand(name, value, parseSubExpressionCommands(name, subCommandLine));
		} else if (name.equals("u")) {
			result = new CookieCommand(name, value);
		} else if (name.equals("k")) {
			result = new CategoryCommand(name, value);
		} else if (name.equals("gcb")) {
			result = new GuiCheckboxCommand(name, value);
		} else if (name.equals("nc")) {
			result = new NumbExConditionalCommand(name, value, parseSubExpressionCommands(name, subCommandLine));
		} else if (name.equals("nm")) {
			result = new NumbExMathematicalCommand(name, value, parseSubExpressionCommands(name, subCommandLine));
		}
		
		return result;
	}
	
	public static List<Command> getCommands(String name, String value) {
		List<Command> results = new ArrayList<>();
		
//		int indexOfSubCommand = value.indexOf("--");
//		String subCommandLine = null;
//		if (indexOfSubCommand >= 0) {
//			subCommandLine = value.substring(indexOfSubCommand);
//			int endSubCommand = subCommandLine.indexOf(Command.DELIMITER);
//			if (endSubCommand >= 0)
//				subCommandLine = subCommandLine.substring(0, endSubCommand);
//		}
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
			for (Pair<String, String> commandSubCommandPair : extractCommandsAndSubCommands(value))
				results.add(new RegExCommand(name, commandSubCommandPair.getLeft(), parseSubExpressionCommands(name, commandSubCommandPair.getRight())));
		} else if (name.equals("u")) {
			result = new CookieCommand(name, value);
		} else if (name.equals("k")) {
			result = new CategoryCommand(name, value);
		} else if (name.equals("gcb")) {
			result = new GuiCheckboxCommand(name, value);
		} else if (name.equals("nc")) {
			for (Pair<String, String> commandSubCommandPair : extractCommandsAndSubCommands(value))
				results.add(new NumbExConditionalCommand(name, commandSubCommandPair.getLeft(), parseSubExpressionCommands(name, commandSubCommandPair.getRight())));
		} else if (name.equals("nm")) {
			for (Pair<String, String> commandSubCommandPair : extractCommandsAndSubCommands(value))
				results.add(new NumbExMathematicalCommand(name, commandSubCommandPair.getLeft(), parseSubExpressionCommands(name, commandSubCommandPair.getRight())));
		}
		
		if (result != null)
			results.add(result);
		
		return results;
	}
	
	private static List<Pair<String, String>> extractCommandsAndSubCommands(String value) {
		Pair<String, String> currentCommandPair = Pair.of("", "");
		List<Pair<String, String>> commandsSubCommands = new ArrayList<>();
		String[] lookAheadSplits = value.split(Command.DELIMITER);
		for (String lookAheadSplit : lookAheadSplits) {
			if (!currentCommandPair.getLeft().isEmpty() /* we want to assign only sub commands */ && lookAheadSplit.trim().startsWith("--")) {
				currentCommandPair = Pair.of(currentCommandPair.getLeft(), currentCommandPair.getRight() + getSubCommandLine(lookAheadSplit));
			} else if (currentCommandPair.getLeft().isEmpty())
				currentCommandPair = Pair.of(getParentCommandLine(lookAheadSplit), currentCommandPair.getRight() + getSubCommandLine(lookAheadSplit));
			else {
				commandsSubCommands.add(currentCommandPair);
				currentCommandPair = Pair.of(getParentCommandLine(lookAheadSplit), getSubCommandLine(lookAheadSplit));					
			}					
		}
		commandsSubCommands.add(currentCommandPair);
		return commandsSubCommands;
	}
	
	private static String getParentCommandLine(String value) {
		String parentCommandLine = value;
		int indexOfSubCommand = value.indexOf("--");
		if (indexOfSubCommand >= 0)
			parentCommandLine = value.substring(0, indexOfSubCommand);		
		return parentCommandLine;
	}
	
	private static String getSubCommandLine(String value) {
		String subCommandLine = "";
		int indexOfSubCommand = value.indexOf("--");
		if (indexOfSubCommand >= 0) {
			subCommandLine = value.substring(indexOfSubCommand);
			int endSubCommand = subCommandLine.indexOf(Command.DELIMITER);
			if (endSubCommand >= 0)
				subCommandLine = subCommandLine.substring(0, endSubCommand);
		}
		return subCommandLine;
	}
	
	private static List<ExpressionCommand>parseSubExpressionCommands(String parentName, String input) {
		if (input == null)
			return null;
		List<ExpressionCommand> result = new ArrayList<>();

		ListIterator<String> argvIter = (ListIterator<String>) Arrays.asList(input.split(" ")).listIterator();

		while (argvIter.hasNext()) {			
			String cmd = (String) argvIter.next();						
//			logger.finest("cmd: " + cmd);
			String value = null;
			if (cmd.startsWith("--")) {
				cmd = cmd.substring(2);
				value = valueLookAhead(argvIter);
			} else {
				return null;
			}
			Command command = CommandFactory.getCommand(cmd, value);
			command.setName(parentName + "_" + cmd);
			if (command instanceof ExpressionCommand)
				result.add((ExpressionCommand)command);
		}

		return result;
	}

	/**
	 * Looks for values corresponding to a command
	 * @param cmdIter The command iterator
	 * @return String (value's corresponding to a command separated by a Command DELIMITER)
	 */
	private static String valueLookAhead(ListIterator<String> cmdIter) {
		String result = "";

		while (cmdIter.hasNext() ) {			
			String cmd = (String) cmdIter.next();
			if (!cmd.startsWith("--") && !cmd.startsWith("-")) {
				if (!result.isEmpty()) {
					result += Command.DELIMITER;
				}
				result += cmd;
			} else {
				cmdIter.previous();
				break;
			}			
		}		

		return result;
	}
}
