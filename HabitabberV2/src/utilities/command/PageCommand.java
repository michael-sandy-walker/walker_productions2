package utilities.command;

import java.util.ArrayList;
import java.util.List;

public class PageCommand extends Command {
	
	private static List<String> pageList = null;
	
	public PageCommand(String name, String value) {
		super(name, value);
		pageList = new ArrayList<String>();
		for (String v : value.split(PageCommand.DELIMITER)) {
			if (!v.startsWith("http")) {
				v = "http://" + v;
				super.setValue(v);
			}
			pageList.add(v);
		}	
	}
	
	public static List<String> getPageList() {
		return pageList;
	}

}
