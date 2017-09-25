package view.button;

import javafx.scene.layout.GridPane;
import view.HabitabberGUI;
import view.action.AddSubExAction;
import view.field.BabyField;

public class AddSubExButton extends PapaButton {
	
	BabyField babyField;
	
	public AddSubExButton(HabitabberGUI gui, GridPane grid, BabyField babyField) {
		this("Add SubEx", gui, grid, babyField);
	}
	
	// babyField is the parent field of the field to add
	public AddSubExButton(String name, HabitabberGUI gui, GridPane grid, BabyField babyField) {
		super(name, new AddSubExAction(gui, name, grid, babyField));
		this.babyField = babyField;
	}
}
