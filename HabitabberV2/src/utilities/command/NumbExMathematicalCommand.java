package utilities.command;

import java.util.List;

public class NumbExMathematicalCommand extends NumbExCommand {
	
	public NumbExMathematicalCommand(String name, String value, List<ExpressionCommand> subExpressions) {
		super(name, value, subExpressions);
	}

	@Override
	public NumbExOperationType getOperationType() {
		return NumbExOperationType.MATHEMATICAL;
	}

	@Override
	public Object evaluate(String input) {
		// TODO Auto-generated method stub
		return null;
	}

}
