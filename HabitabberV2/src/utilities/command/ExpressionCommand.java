package utilities.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ExpressionCommand extends Command {
	private ExpressionCommand parentExpressionCommand = null;
	
	private static List<String> expressionList = null;
	private static Map<ExpressionCommand, List<ExpressionCommand>> subExpressions = new LinkedHashMap<>();
	
	public ExpressionCommand(String name, String value, List<ExpressionCommand> subExpressions) {
		this(name, value, subExpressions, null);
	}
	
	public ExpressionCommand(String name, String value, List<ExpressionCommand> subExpressionValues, ExpressionCommand parentExpressionCommand) {
		super(name, value);
		expressionList = new ArrayList<String>();
		for (String v : value.split(RegExCommand.DELIMITER)) {
			expressionList.add(v);
		}		
//		ExpressionCommand.setSubExpressions(subExpressions); // TODO: associate with parent expression! (11-09-2017)
		if (subExpressionValues != null) {
			for (ExpressionCommand ec : subExpressionValues)
				if (ec.getParentExpressionCommand() == null)
					ec.setParentExpressionCommand(this);
			subExpressions.put(this, subExpressionValues);
		}
	}
	
	public static List<String> getExpressionList() {
		return expressionList;
	}

	public abstract ExpressionType getExpressionType();

	public ExpressionCommand getParentExpressionCommand() {
		return parentExpressionCommand;
	}

	public void setParentExpressionCommand(ExpressionCommand parentExpressionCommand) {
		this.parentExpressionCommand = parentExpressionCommand;
	}
	
	public static Map<ExpressionCommand, List<ExpressionCommand>> getSubExpressionMap() {
		return subExpressions;
	}

	public static void setSubExpressionMap(Map<ExpressionCommand, List<ExpressionCommand>> subExpressions) {
		ExpressionCommand.subExpressions = subExpressions;
	}
	
	public abstract Object evaluate(String input);
}
