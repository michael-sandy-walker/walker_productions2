package view.action;

import javafx.scene.layout.GridPane;
import view.HabitabberGUI;
import view.field.BabyField;

public class AddRegExAction extends PapaAction {
	GridPane grid;
	String name;
	public AddRegExAction(HabitabberGUI gui, String name, GridPane grid) {
		super(gui);		
		this.grid = grid;
		this.name = name;
	}
	
	@Override
	public void performAction() {
		gui.addRegExField(grid, "Regex", null, false);
	}
}
