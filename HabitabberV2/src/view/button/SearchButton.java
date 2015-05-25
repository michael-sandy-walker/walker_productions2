package view.button;

import view.action.SearchAction;

public class SearchButton extends PapaButton {
	
	public SearchButton() {
		this("Search");
	}

	public SearchButton(String name) {
		super(name, new SearchAction());
	}

}
