package view.field;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import view.button.AddSubExButton;
import view.button.RemoveButton;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class BabyField extends PapaField {
	
	List<Node> nodeList;
	
	int hIndex;
	
	private final int type;
	
	private RemoveButton removeButton;
	
	private AddSubExButton addSubExButton;
	
	private Control label;
	private GridPane grid;
	
	private static 	Map<String, BabyField> fieldMap = new LinkedHashMap<String, BabyField>();
	
	public BabyField(String name, String text, int type) {
		super(name, text);
		this.type = type;
	}
	
	public BabyField(String name, String text, int type, int hIndex, GridPane grid) {
		super(name, text, "", hIndex);
		this.type = type;
		this.grid = grid;
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
	
	public void setLabel(Control label) {
		this.label = label;
	}
	
	public Control getLabel() {
		return label;
	}

	public AddSubExButton getAddSubExButton() {
		return addSubExButton;
	}

	public void setAddSubExButton(AddSubExButton addSubExButton) {
		this.addSubExButton = addSubExButton;
	}

	private static Map<String, BabyField> getFieldMap() {
		return fieldMap;
	}

	private static void setFieldMap(Map<String, BabyField> fieldMap) {
		BabyField.fieldMap = fieldMap;
	}
	
	public static BabyField getFieldOfFieldMap(String key) {
		return fieldMap.get(key);
	}
	
	protected static BabyField removeFieldOfBabyFieldMap(String key) {
//		PapaField.removeFieldOfFieldMap(key);
		int startHIndex = fieldMap.get(key).getHIndex();
		BabyField result = fieldMap.remove(key);
		for (BabyField babyField : fieldMap.values()) {
			int hIndex = babyField.getHIndex();
			if (hIndex > startHIndex)
				babyField.setHIndex(hIndex-1);
		}
		return result;
	}
	
	protected BabyField addFieldToBabyFieldMap(String key) {
		return addFieldToBabyFieldMap(key, getHIndex());
	}
	
	protected BabyField addFieldToBabyFieldMap(String key, int hIndex) {
		boolean hIndexAlreadyExists = false;
		if (hIndex != -1) {
			for (BabyField babyFieldTmp : fieldMap.values())
				if (babyFieldTmp.getHIndex() == hIndex) {
					hIndexAlreadyExists = true;
					break;
				}
			if (hIndexAlreadyExists)
				for (BabyField babyFieldTmp : fieldMap.values()) {
					int hIndexTmp = babyFieldTmp.getHIndex(); 
					if (hIndexTmp >= hIndex)
						babyFieldTmp.setHIndex(hIndexTmp + 1);
					for (Node node : babyFieldTmp.getRegExRowNodes())
						grid.setRowIndex(node, babyFieldTmp.getHIndex());
				}
			setHIndex(hIndex);
		}
		return fieldMap.put(key, this);
	}
	
	public static Collection<BabyField> getValuesOfBabyFieldMap() {
		return fieldMap.values();
	}
}
