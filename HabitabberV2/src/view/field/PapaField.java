package view.field;

import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.TextField;
import view.HabitabberGUI;

public abstract class PapaField {
	private TextField textField;
	private String name;
	private String id;

	private static 	Map<String, PapaField> fieldMap = new TreeMap<String, PapaField>();
	
	public static final String SEPARATOR = "-#-";

	public PapaField(String name) {
		this(name, "");
	}

	public PapaField(String name, String text) {		
		this(name, "", "");
	}
	
	public PapaField(String name, String text, String id) {
		if (id != null && !id.isEmpty())
			this.id = id;
		else
			setId(this.getClass().getSimpleName() + SEPARATOR + name);
		setTextField(new TextField(text));
		setName(name);
		fieldMap.put(getId(), this);
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

	public static Map<String, PapaField> getFieldMap() {
		return fieldMap;
	}
	
	public static Map<String, PapaField> getFieldMapByIdType(Class<?> clazz) {
		Map<String, PapaField> subMap = new TreeMap<String, PapaField>();;
		
		for (String key : fieldMap.keySet())
			if (key.contains(clazz.getSimpleName()))
				subMap.put(key, fieldMap.get(key));
		
		return subMap;
	}
	
	public static int retrieveFirstFreeHIndex(int type) {		
		return retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(type), type);
	}

	public static int retrieveFirstFreeHIndex(int index, int type) {
		return retrieveFirstFreeHIndex(index, fieldMap, type);
	}
	
	public static int retrieveFirstFreeHIndex(Map<String, PapaField> fieldMap, int type) {		
		return retrieveFirstFreeHIndex(HabitabberGUI.getHIndexOffset(type), fieldMap, type);
	}
	
	public static int retrieveFirstFreeHIndex(int index, Map<String, PapaField> fieldMap, int type) {		
		int freeHIndex = index + 1;
		Integer prevHIndex = null;

		Map<Integer, PapaField> orderedMap = new TreeMap<Integer, PapaField>();

		for (Map.Entry<String, PapaField> entry : fieldMap.entrySet()) {
			PapaField field = (PapaField) entry.getValue();
			if (field.getType() == type) {
				String keyStr = entry.getKey();
				orderedMap.put(Integer.parseInt(keyStr.substring(keyStr.indexOf(PapaField.SEPARATOR) + PapaField.SEPARATOR.length())), entry.getValue());
			}			
		}

		for (PapaField field : orderedMap.values()) {
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
