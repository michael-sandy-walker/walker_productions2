package view.action;

import javafx.scene.layout.GridPane;
import view.HabitabberGUI;

public class AddRegExAction extends PapaAction {
	HabitabberGUI gui;
	GridPane grid;
	String name;
	public AddRegExAction(String name, HabitabberGUI gui, GridPane grid) {
		this.gui = gui;
		this.grid = grid;
		this.name = name;
	}
	
	@Override
	public void performAction() {
		gui.addRegExField(grid, name);
	}
}
