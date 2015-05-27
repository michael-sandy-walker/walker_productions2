package view.field;

import java.util.Map;
import java.util.TreeMap;

import view.HabitabberGUI;
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

	public static int retrieveFirstFreeHIndex() {
		int freeHIndex = HabitabberGUI.getHIndexOffset() + 1;
		Integer prevHIndex = null;

		Map<Integer, PapaField> orderedMap = new TreeMap<Integer, PapaField>();

		for (Map.Entry<String, PapaField> entry : getFieldMap().entrySet()) {
			PapaField field = (PapaField) entry.getValue();
			if (field instanceof RegExField) {
				orderedMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
			}			
		}

		for (PapaField field : orderedMap.values()) {
			if (field instanceof RegExField) {
				RegExField regExField = (RegExField) field;				
				if (prevHIndex == null) {
					prevHIndex = regExField.getHIndex();
					if (prevHIndex > freeHIndex) {
						break;
					}
				}				

				if (regExField.getHIndex() > prevHIndex + 1) {
					freeHIndex = prevHIndex + 1;
					break;
				} else {
					freeHIndex++;
				}

				prevHIndex = regExField.getHIndex();
			}
		}
		return freeHIndex;
	}
}
