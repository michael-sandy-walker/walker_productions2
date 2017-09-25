package view.field;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import view.HabitabberGUI;

public class RegExField extends BabyField {
	
	private RegExField parent;
	
	public RegExField(String name, String text) {
		super(name, text, HabitabberGUI.REGEX_TYPE);
	}
	
	public RegExField(String name, String text, int hIndex, GridPane grid) {
		super(name, text, HabitabberGUI.REGEX_TYPE, hIndex, grid);
	}
			
	public static int retrieveFirstFreeHIndex() {
		return RegExField.retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(HabitabberGUI.REGEX_TYPE));
	}
	
	public static int retrieveFirstFreeHIndex(int index) {
		return retrieveFirstFreeHIndex(index, PapaField.getValuesOfFieldMap(), HabitabberGUI.REGEX_TYPE);
	}

	public RegExField getParent() {
		return parent;
	}

	public void setParent(RegExField parent) {
		this.parent = parent;
	}
}
