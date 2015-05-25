package view.button;

import view.action.StopAction;

public class StopButton extends PapaButton {
	public StopButton() {
		this("Stop");
	}

	public StopButton(String name) {
		super(name, new StopAction());
	}
}
