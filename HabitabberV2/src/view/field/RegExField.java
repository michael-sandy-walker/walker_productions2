package view.field;

import view.HabitabberGUI;

public class RegExField extends BabyField {
	
	public RegExField(String name, String text) {
		super(name, text, HabitabberGUI.REGEX_TYPE);
	}
			
	public static int retrieveFirstFreeHIndex() {
		return RegExField.retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(HabitabberGUI.REGEX_TYPE));
	}
	
	public static int retrieveFirstFreeHIndex(int index) {
		return retrieveFirstFreeHIndex(index, PapaField.getFieldMap(), HabitabberGUI.REGEX_TYPE);
	}
}
