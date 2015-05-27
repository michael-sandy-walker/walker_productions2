package view.button;

import javafx.scene.layout.GridPane;
import view.HabitabberGUI;
import view.action.AddRegExAction;

public class AddRegExButton  extends PapaButton {	
	
	public AddRegExButton(HabitabberGUI gui, GridPane grid) {
		this("Add RegEx", gui, grid);
	}

	public AddRegExButton(String name, HabitabberGUI gui, GridPane grid) {
		super(name, new AddRegExAction(name, gui, grid));
	}
}
