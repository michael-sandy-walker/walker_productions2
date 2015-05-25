package utilities.command;

public class MaxSearchCommand extends Command {
	
	private static Integer maxSearches = 10;

	public MaxSearchCommand(String name, String value) {
		super(name, value);
		try {
			maxSearches = Integer.parseInt(value);
		} catch (NumberFormatException|NullPointerException e) {
			System.out.println("The value of the maximum number of searches has to be numeric.");
		}
	}
	
	public static Integer getMaxSearches() {
		return maxSearches;
	}

}
