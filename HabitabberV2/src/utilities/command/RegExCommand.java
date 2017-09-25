package utilities.command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegExCommand extends ExpressionCommand {
	
	private List<ExpressionCommand> subExpressions = null;

	public RegExCommand(String name, String value, List<ExpressionCommand> subExpressions) {
		super(name, value, subExpressions);
		this.subExpressions = subExpressions;
	}

	@Override
	public ExpressionType getExpressionType() {
		return ExpressionType.REGEX;
	}

	@Override
	public Object evaluate(String input) {
		return null;
	}

	public List<ExpressionCommand> getSubExpressions() {
		return subExpressions;
	}

	public void setSubExpressions(List<ExpressionCommand> subExpressions) {
		this.subExpressions = subExpressions;
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
