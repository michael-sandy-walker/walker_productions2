package result;

public class PageFactory {
	public static MainPage getPage(String content, boolean subPage) {
		MainPage result;
		if (subPage)
			result = new SubPage(content);
		else 
			result = new MainPage(content);
		return result;
	}
}
