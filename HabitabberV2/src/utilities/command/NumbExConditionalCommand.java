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

	@Override
	public Object evaluate(String input) {
		Boolean result = false;
		String[] elements = this.value.split(Command.DELIMITER);
		if (elements.length == 2) {
			try {
				String operator = elements[0];
				int value = Integer.parseInt(elements[1]);
				int inputValue = Integer.parseInt(input);
				if (operator.equals(NumbExConditionalType.EQUALS))
					result = inputValue == value;
				else if (operator.equals(NumbExConditionalType.GREATER))
					result = inputValue > value;
				else if (operator.equals(NumbExConditionalType.GREATER_THAN))
					result = inputValue >= value;
				else if (operator.equals(NumbExConditionalType.SMALLER))
					result = inputValue < value;
				else if (operator.equals(NumbExConditionalType.SMALLER_THAN))
					result = inputValue <= value;
			} catch (NumberFormatException e) 
			{ 
				// Do nothing for the moment TODO: catch these kind of exceptions 
			}
		}
			
		return result;
	}

}
