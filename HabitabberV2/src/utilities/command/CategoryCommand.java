package utilities.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import utilities.Token;

public class CategoryCommand extends Command {
	
	
	public static final String KEY_DELIMITER = "§§§--ßßßß";
	public static String VALUE_DELIMITER = "§§§-ßßßß";
	
	public static Map<String, String> CATEGORY_MAP = new LinkedHashMap<String, String>();
	
	public CategoryCommand(String name, String value) {
		super(name, value);		
		if (value != null && ! value.isEmpty()) {
			CATEGORY_MAP = new LinkedHashMap<>();
			for (String v : value.split(CategoryCommand.VALUE_DELIMITER)) {
				String[] keyValuePair = v.split(CategoryCommand.KEY_DELIMITER);
				CATEGORY_MAP.put(keyValuePair[0], keyValuePair[1]);
			}
		}		
	}

}
