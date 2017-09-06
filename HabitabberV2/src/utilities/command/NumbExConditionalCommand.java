package utilities.command;

import java.util.List;

public class NumbExConditionalCommand extends NumbExCommand {
	
	public NumbExConditionalCommand(String name, String value, List<ExpressionCommand> subExpressions) {
		super(name, value, subExpressions);
	}

	@Override
	public NumbExOperationType getOperationType() {
		return NumbExOperationType.CONDITIONAL;
	}

}
