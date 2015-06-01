package view.button;

import result.Page;
import view.HabitabberGUI;
import view.action.PopupAction;

public class PopupButton extends PapaButton {
	
	public PopupButton(HabitabberGUI gui) {
		this(gui, "Show content", new Page());
	}
	
	public PopupButton(HabitabberGUI gui, String name, Page page) {
		super(name, new PopupAction(gui, name, page));
	}

}
