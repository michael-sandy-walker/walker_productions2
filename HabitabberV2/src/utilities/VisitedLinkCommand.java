package utilities;

public class VisitedLinkCommand extends Command {
	
	private static String visitedLinkFileName = null;

	public VisitedLinkCommand(String name, String value) {
		super(name, value);
		visitedLinkFileName = value;
	}
	
	public static String getVisitedLinkFileName() {
		return visitedLinkFileName;
	}

}
