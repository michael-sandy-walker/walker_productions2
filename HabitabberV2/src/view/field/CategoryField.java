package view.field;

import java.util.Map;
import java.util.TreeMap;

import view.HabitabberGUI;

public class CategoryField extends BabyField {
		
		private static 	Map<String, PapaField> fieldMap = new TreeMap<String, PapaField>();

		public CategoryField(String name, String text) {
			super(name, text);
			CategoryField.fieldMap.put(name, this);
		}
				
		public static int retrieveFirstFreeHIndex() {
			return CategoryField.retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(HabitabberGUI.CATEGORY_TYPE));
		}
		
		public static int retrieveFirstFreeHIndex(int index) {
			return retrieveFirstFreeHIndex(index, fieldMap);
		}
}
