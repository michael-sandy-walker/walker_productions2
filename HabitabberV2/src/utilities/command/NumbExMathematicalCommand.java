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

}
