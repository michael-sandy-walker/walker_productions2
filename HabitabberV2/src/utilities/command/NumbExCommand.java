package utilities.command;

import java.util.List;

public abstract class NumbExCommand extends ExpressionCommand {
	
	public NumbExCommand(String name, String value, List<ExpressionCommand> subExpressions) {
		super(name, value, subExpressions);
	}

	@Override
	public ExpressionType getExpressionType() {
		return ExpressionType.NUMBEX;
	}

	public abstract NumbExOperationType getOperationType();

}
