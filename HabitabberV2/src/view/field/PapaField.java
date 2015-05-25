package view.field;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextField;

public class PapaField {
	private TextField textField;
	private String name;
	
	private static 	List<PapaField> fieldList = new ArrayList<PapaField>();
	
	public PapaField(String name) {
		this(name, "");
	}
	
	public PapaField(String name, String text) {
		setTextField(new TextField(text));
		setName(name);
		fieldList.add(this);
	}

	/**
	 * @return the textField
	 */
	public TextField getTextField() {
		return textField;
	}

	/**
	 * @param textField the textField to set
	 */
	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public static List<PapaField> getFieldList() {
		return fieldList;
	}
}
