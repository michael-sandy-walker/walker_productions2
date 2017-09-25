package view.field;

import view.HabitabberGUI;

public class CategoryField extends BabyField {

	public CategoryField(String name, String text) {
		super(name, text, HabitabberGUI.CATEGORY_TYPE);
	}

	public static int retrieveFirstFreeHIndex() {
		return CategoryField.retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(HabitabberGUI.CATEGORY_TYPE));
	}

	public static int retrieveFirstFreeHIndex(int index) {
		return retrieveFirstFreeHIndex(index, PapaField.getValuesOfFieldMap(), HabitabberGUI.CATEGORY_TYPE);
	}
}
