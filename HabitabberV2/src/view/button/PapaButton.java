package view.button;

import view.HabitabberGUI;
import view.action.PapaAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class PapaButton {
	
	Button button;
	private PapaAction action;
	
	public PapaButton(HabitabberGUI gui, String name) {
		this(name, new PapaAction(gui));
	}
	
	public PapaButton(String name, PapaAction action) {		
		this.setAction(action);
		button = new Button(name);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				action.performAction();
			}
		});
	}
	
	public Button getButton() {
		return button;
	}

	/**
	 * @return the action
	 */
	public PapaAction getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(PapaAction action) {
		this.action = action;
	}
}
