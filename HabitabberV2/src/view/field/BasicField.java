package view.field;

public class BasicField extends PapaField {
	
	public BasicField(String name) {
		super(name, "");
	}

	public BasicField(String name, String text) {
		super(name, text);
	}

	@Override
	public int getType() {
		return -1;
	}

}
