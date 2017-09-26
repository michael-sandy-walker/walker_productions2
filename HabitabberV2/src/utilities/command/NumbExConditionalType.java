package utilities.command;

public enum NumbExConditionalType { //TODO: Implement (06-SEP-2017)
	EQUALS("=="),
	GREATER(">"),
	GREATER_THAN(">="),
	SMALLER("<"),
	SMALLER_THAN("<=");
	
	private final String strValue;
	
	NumbExConditionalType(String strValue) {
		this.strValue = strValue;
	}

	public String getStrValue() {
		return strValue;
	}
	
	public static NumbExConditionalType getTypeByValue(String value) {
		for (NumbExConditionalType numbExConditionalType : NumbExConditionalType.values())
			if (numbExConditionalType.getStrValue().equals(value))
				return numbExConditionalType;
		return null;
	}
}
