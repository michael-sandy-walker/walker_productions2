package view.action;

import javafx.scene.layout.GridPane;
import view.HabitabberGUI;
import view.field.BabyField;

public class AddSubExAction extends PapaAction {
	
	GridPane grid;
	String name;
	BabyField babyField;
	
	// babyField is the parent field of the field to add
	public AddSubExAction(HabitabberGUI gui, String name, GridPane grid, BabyField babyField) {
		super(gui);		
		this.grid = grid;
		this.name = name;
		this.babyField = babyField;
	}
	
	// babyField is the parent field of the field to add
	@Override
	public void performAction() {
		gui.addRegExField(grid, "Subex", babyField, true);
	}

}
