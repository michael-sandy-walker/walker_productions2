package view.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.Node;

import javafx.scene.control.TextField;
import view.HabitabberGUI;

public abstract class PapaField {
	private TextField textField;
	private String name;
	private String id;

	private static Map<String, PapaField> fieldMap = new LinkedHashMap<String, PapaField>();
	
	public static final String SEPARATOR = "-#-";

	public PapaField(String name) {
		this(name, "");
	}

	public PapaField(String name, String text) {
		this(name, text, "");
	}
	
	public PapaField(String name, String text, String id) {
		if (id != null && !id.isEmpty())
			this.id = id;
		else
			setId(this.getClass().getSimpleName() + SEPARATOR + name);
		setTextField(new TextField(text));
		setName(name);
		addFieldToFieldMap(getId(), this);
	}
	
	public PapaField(String name, String text, String id, int hIndex) {
		if (id != null && !id.isEmpty())
			this.id = id;
		else
			setId(this.getClass().getSimpleName() + SEPARATOR + name);
		setTextField(new TextField(text));
		setName(name);
		addFieldToFieldMap(getId(), this, hIndex);
	}

	/**
	 * @return the textField
	 */
	public TextField getTextField() {
		return textField;
	}

	/**
	 * @param textField the textField to set
	 */
	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public static PapaField getFieldOfFieldMap(String key) {
		return fieldMap.get(key);
	}
	
	public static PapaField removeFieldOfFieldMap(String key) {
		if (fieldMap.get(key) instanceof BabyField)
			BabyField.removeFieldOfBabyFieldMap(key);
		return fieldMap.remove(key);
	}
	
	public PapaField addFieldToFieldMap(String key, PapaField papaField) {		
		return addFieldToFieldMap(key, papaField, -1);
	}
	
	public PapaField addFieldToFieldMap(String key, PapaField papaField, int hIndex) {		
		if (papaField instanceof BabyField) {
			BabyField babyField = (BabyField) papaField;
			babyField.addFieldToBabyFieldMap(key, hIndex);
		}
		return fieldMap.put(key, papaField);
	}
	
	public static Collection<PapaField> getValuesOfFieldMap() {
		return fieldMap.values();
	}
	
	public static Map<String, PapaField> getFieldMapByIdType(Class<?> clazz) {
		Map<String, PapaField> subMap = new TreeMap<String, PapaField>();;
		
		for (String key : fieldMap.keySet())
			if (key.contains(clazz.getSimpleName()))
				subMap.put(key, fieldMap.get(key));
		
		return subMap;
	}
	
	public static void resetFields() {
		fieldMap.clear();
		HabitabberGUI.resetHIndex();
	}
	
	public static int retrieveFirstFreeHIndex(int type) {		
		return retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(type), type);
	}

	public static int retrieveFirstFreeHIndex(int index, int type) {
		return retrieveFirstFreeHIndex(index, getValuesOfFieldMap(), type);
	}
	
	public static int retrieveFirstFreeHIndex(Collection<PapaField> fieldMapValues, int type) {		
		return retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(type), fieldMapValues, type);
	}
	
	public static int retrieveFirstFreeHIndex(int index, Collection<PapaField> fieldMapValues, int type) {		
		int freeHIndex = index + 1;
		Integer prevHIndex = null;

		List<PapaField> fieldList = new ArrayList<>();

		for (PapaField papaField : fieldMapValues) {
			if (papaField.getType() == type)
				fieldList.add(papaField);
		}

		for (PapaField field : fieldList) {
			if (field instanceof BabyField) {
				BabyField babyField = (BabyField) field;				
				if (prevHIndex == null) {
					prevHIndex = babyField.getHIndex();
					if (prevHIndex > freeHIndex) {
						break;
					}
				}				

				if (babyField.getHIndex() > prevHIndex + 1) {
					freeHIndex = prevHIndex + 1;
					break;
				} else {
					freeHIndex++;
				}

				prevHIndex = babyField.getHIndex();
			}
		}
		return freeHIndex;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public abstract int getType();

}
