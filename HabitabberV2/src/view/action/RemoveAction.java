package view.action;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import view.HabitabberGUI;
import view.field.BabyField;
import view.field.PapaField;

public class RemoveAction extends PapaAction {
	
	String name;
	
	public RemoveAction(HabitabberGUI gui, String name) {
		super(gui);
		this.name = name;
	}
	
	@Override
	public void performAction() {
		BabyField field = (BabyField) PapaField.getFieldMap().get(name);
		Parent parent = field.getTextField().getParent(); // Grid
		GridPane grid = (GridPane) parent;
		for (Node node : field.getRegExRowNodes()) {
			grid.getChildren().remove(node);
		}
		PapaField.getFieldMap().remove(name);
	}
}
