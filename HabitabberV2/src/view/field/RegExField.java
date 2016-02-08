package view.field;

import java.util.Map;
import java.util.TreeMap;

import view.HabitabberGUI;

public class RegExField extends BabyField {
	
	private static 	Map<String, PapaField> fieldMap = new TreeMap<String, PapaField>();

	public RegExField(String name, String text) {
		super(name, text);
		RegExField.fieldMap.put(name, this);
	}
			
	public static int retrieveFirstFreeHIndex() {
		return RegExField.retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(HabitabberGUI.REGEX_TYPE));
	}
	
	public static int retrieveFirstFreeHIndex(int index) {
		return retrieveFirstFreeHIndex(index, fieldMap);
	}
}
