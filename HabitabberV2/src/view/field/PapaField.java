package view.field;

import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.TextField;
import view.HabitabberGUI;

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
		return retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(HabitabberGUI.REGEX_TYPE));
	}

	public static int retrieveFirstFreeHIndex(int index) {
		return retrieveFirstFreeHIndex(index, fieldMap);
	}
	
	public static int retrieveFirstFreeHIndex(Map<String, PapaField> fieldMap) {		
		return retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(HabitabberGUI.REGEX_TYPE), fieldMap);
	}
	
	public static int retrieveFirstFreeHIndex(int index, Map<String, PapaField> fieldMap) {		
		int freeHIndex = index + 1;
		Integer prevHIndex = null;

		Map<Integer, PapaField> orderedMap = new TreeMap<Integer, PapaField>();

		for (Map.Entry<String, PapaField> entry : fieldMap.entrySet()) {
			PapaField field = (PapaField) entry.getValue();
			if (field instanceof BabyField) {
				orderedMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
			}			
		}

		for (PapaField field : orderedMap.values()) {
			if (field instanceof BabyField) {
				BabyField babyField = (BabyField) field;				
				if (prevHIndex == null) {
					prevHIndex = babyField.getHIndex();
					if (prevHIndex > freeHIndex) {
						break;
					}
				}				

				if (babyField.getHIndex() > prevHIndex + 1) {
					freeHIndex = prevHIndex + 1;
					break;
				} else {
					freeHIndex++;
				}

				prevHIndex = babyField.getHIndex();
			}
		}
		return freeHIndex;
	}
}
