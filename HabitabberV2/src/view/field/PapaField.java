package view.field;

import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.TextField;

public class PapaField {
	private TextField textField;
	private String name;
	
	private static 	Map<String, PapaField> fieldMap = new TreeMap<String, PapaField>();
	
	public PapaField(String name) {
		this(name, "");
	}
	
	public PapaField(String name, String text) {
		setTextField(new TextField(text));
		setName(name);
//		fieldList.add(this);
		fieldMap.put(name, this);
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
	
	public static Map<String, PapaField> getFieldMap() {
		return fieldMap;
	}
}
