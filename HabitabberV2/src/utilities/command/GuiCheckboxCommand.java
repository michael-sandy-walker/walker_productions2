package utilities.command;

import java.util.LinkedHashMap;
import java.util.Map;

public class GuiCheckboxCommand extends Command {

	public static final String VALUE_SEPARATOR = ":";
	public static Map<String, Boolean> CHECKBOX_MAP = new LinkedHashMap<>();

	public GuiCheckboxCommand(String name, String value) {
		super(name, value);		
		CHECKBOX_MAP = new LinkedHashMap<>();
		if (value != null && ! value.isEmpty()) {
			for (String keyValue : value.split(Command.DELIMITER)) {
				String[] keyValuePair = keyValue.split(VALUE_SEPARATOR);
				CHECKBOX_MAP.put(keyValuePair[0], keyValuePair[1].equals("y") ? true : false);
			}
		}
	}

}
