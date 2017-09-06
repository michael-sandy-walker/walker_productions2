package utilities.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ExpressionCommand extends Command {
	private static List<String> expressionList = null;
	private static List<ExpressionCommand> subExpressions = new ArrayList<>();
	
	public ExpressionCommand(String name, String value, List<ExpressionCommand> subExpressions) {
		super(name, value);
		expressionList = new ArrayList<String>();
		for (String v : value.split(RegExCommand.DELIMITER)) {
			expressionList.add(v);
		}		
		this.subExpressions = subExpressions;
	}
	
	public static List<String> getExpressionList() {
		return expressionList;
	}

	public static List<ExpressionCommand> getSubExpressions() {
		return subExpressions;
	}
	
	public abstract ExpressionType getExpressionType();
}
