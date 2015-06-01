package view.button;

import view.HabitabberGUI;
import view.action.StopAction;

public class StopButton extends PapaButton {
	public StopButton(HabitabberGUI gui) {
		this(gui, "Stop");
	}

	public StopButton(HabitabberGUI gui, String name) {
		super(name, new StopAction(gui));
	}
}
