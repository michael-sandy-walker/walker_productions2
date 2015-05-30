package result;

public class PageFactory {
	public static Page getPage(String content, boolean subPage) {
		Page result;
		if (subPage)
			result = new SubPage(content);
		else 
			result = new Page(content);
		return result;
	}
}
