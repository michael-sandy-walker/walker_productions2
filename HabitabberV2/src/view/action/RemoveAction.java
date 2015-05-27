package view.action;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import view.field.PapaField;
import view.field.RegExField;

public class RemoveAction extends PapaAction {
	
	String name;
	
	public RemoveAction(String name) {
		this.name = name;
	}
	
	@Override
	public void performAction() {
		RegExField field = (RegExField) PapaField.getFieldMap().get(name);
		Parent parent = field.getTextField().getParent(); // Grid
		GridPane grid = (GridPane) parent;
		for (Node node : field.getRegExRowNodes()) {
			grid.getChildren().remove(node);
		}
		PapaField.getFieldMap().remove(name);
	}
}
