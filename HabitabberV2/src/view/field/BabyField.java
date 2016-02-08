package view.field;

import java.util.List;

import javafx.scene.Node;

public abstract class BabyField extends PapaField {
	
	List<Node> nodeList;
	
	int hIndex;
	
	private final int type;
	
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
}
