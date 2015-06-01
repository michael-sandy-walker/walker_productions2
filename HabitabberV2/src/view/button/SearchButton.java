package view.button;

import view.HabitabberGUI;
import view.action.SearchAction;

public class SearchButton extends PapaButton {
	
	public SearchButton(HabitabberGUI gui) {
		this(gui, "Search");
	}

	public SearchButton(HabitabberGUI gui, String name) {
		super(name, new SearchAction(gui));
	}

}
