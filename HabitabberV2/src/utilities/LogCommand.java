package utilities;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogCommand extends Command {
	
	private static Level level = Level.INFO;
	
	Logger logger = Logger.getLogger("LogCommand");

	public LogCommand(String name, String value) {
		super(name, value);
		outputLogLevel(value);
		if (value.toLowerCase().equals("FINEST".toLowerCase())) 
			level = Level.FINEST;
		else if (value.toLowerCase().equals("FINER".toLowerCase()))
			level = Level.FINER;
		else if (value.toLowerCase().equals("FINE".toLowerCase()))
			level = Level.FINE;
		else if (value.toLowerCase().equals("CONFIG".toLowerCase()))
			level = Level.CONFIG;
		else if (value.toLowerCase().equals("INFO".toLowerCase()))
			level = Level.INFO;
		else if (value.toLowerCase().equals("WARNING".toLowerCase()))
			level = Level.WARNING;
		else if (value.toLowerCase().equals("SEVERE".toLowerCase()))
			level = Level.SEVERE;
	}
	
	public void outputLogLevel(String level) {
		logger.info("Seting logging level to "+ level);
	}
	
	public static Level getLevel() {
		return level;
	}

}
