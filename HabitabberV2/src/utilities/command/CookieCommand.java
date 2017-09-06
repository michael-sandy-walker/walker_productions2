package utilities.command;

public class CookieCommand extends Command {
	private static boolean useCookies = false;
	
	public CookieCommand(String name, String value) {
		super(name, value);
		if (value != null) {
			useCookies = value.toLowerCase().equals("y");
		} else {
			useCookies = true;
		}
	}
	
	public static boolean isUseCookies() {
		return useCookies;
	}
}
