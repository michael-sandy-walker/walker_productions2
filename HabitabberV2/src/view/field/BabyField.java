package view.field;

import java.util.List;

import javafx.scene.Node;

public class BabyField extends PapaField {
	
	List<Node> nodeList;
	
	int hIndex;
	
	public BabyField(String name, String text) {
		super(name, text);
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
}
