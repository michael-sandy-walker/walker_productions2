package utilities.command;

import java.util.List;

public class RegExCommand extends ExpressionCommand {

	public RegExCommand(String name, String value, List<ExpressionCommand> subExpressions) {
		super(name, value, subExpressions);
	}

	@Override
	public ExpressionType getExpressionType() {
		return ExpressionType.REGEX;
	}

//	private static List<String> regExList = null;
//	
//	public RegExCommand(String name, String value) {
//		super(name, value);
//		regExList = new ArrayList<String>();
//		for (String v : value.split(RegExCommand.DELIMITER)) {
//			regExList.add(v);
//		}
//	}
//	
//	public static List<String> getRegExList() {
//		return regExList;
//	}

}
