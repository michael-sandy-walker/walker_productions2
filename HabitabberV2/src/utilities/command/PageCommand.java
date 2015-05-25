package utilities.command;

import java.util.ArrayList;
import java.util.List;

public class PageCommand extends Command {
	
	private static List<String> pageList = null;
	
	public PageCommand(String name, String value) {
		super(name, value);
		pageList = new ArrayList<String>();
		for (String v : value.split(PageCommand.DELIMITER)) {
			pageList.add(v);
		}	
	}
	
	public static List<String> getPageList() {
		return pageList;
	}

}
