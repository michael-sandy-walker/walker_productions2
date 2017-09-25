package utilities.command;

import java.util.ArrayList;
import java.util.List;

public class Command {
	
	public static final String DELIMITER = ";";
	
	protected String name;
	protected String value;
	
	private static final List<String> registeredCommands = new ArrayList<String>();
	
	public Command(String name, String value) {
		this.setName(name);
		this.setValue(value);
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

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	public static List<String> getRegisteredCommands() {
		return registeredCommands;
	}
	
	public static void registerCommand(String cmd) {
		registeredCommands.add(cmd);
	}
}
