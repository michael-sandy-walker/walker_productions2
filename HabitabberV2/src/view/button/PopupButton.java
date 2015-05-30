package view.button;

import result.Page;
import view.action.PopupAction;

public class PopupButton extends PapaButton {
	
	public PopupButton() {
		this("Show content", new Page());
	}
	
	public PopupButton(String name, Page page) {
		super(name, new PopupAction(name, page));
	}

}
