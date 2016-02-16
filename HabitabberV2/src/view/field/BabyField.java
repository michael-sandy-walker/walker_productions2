package view.field;

import java.util.List;

import view.button.RemoveButton;
import javafx.scene.Node;
import javafx.scene.control.Label;

public abstract class BabyField extends PapaField {
	
	List<Node> nodeList;
	
	int hIndex;
	
	private final int type;
	
	private RemoveButton removeButton;
	
	private Label label;
	
	public BabyField(String name, String text, int type) {
		super(name, text);
		this.type = type;
	}
	
	public void setRegExRowNodes(List<Node> nodeList) {
		this.nodeList = nodeList;
	}
	
	public List<Node> getRegExRowNodes() {
		return nodeList;
	}
	
	public void setHIndex(int hIndex) {
		this.hIndex = hIndex;
	}
	
	public int getHIndex() {
		return hIndex;
	}

	@Override
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	public void setRemoveButton(RemoveButton removeButton) {
		this.removeButton = removeButton;
	}
	
	public RemoveButton getRemoveButton() {
		return removeButton;
	}
	
	public void setLabel(Label label) {
		this.label = label;
	}
	
	public Label getLabel() {
		return label;
	}
}
