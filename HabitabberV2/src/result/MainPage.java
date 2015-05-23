package result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainPage {

	protected Map<String, List<Object>> content = new TreeMap<String, List<Object>>();

	protected String unparsedContent;

	protected String url;
	
	protected List<SubPage> subPageList = new ArrayList<SubPage>();

	public MainPage() {
		unparsedContent = "";
	}

	public MainPage(String unparsedContent) {
		this.unparsedContent = unparsedContent;
	}

	public void setContent(Map<String, List<Object>> content) {
		this.content = content;
	}

	public Map<String, List<Object>> getContent() {
		return content;		
	}

	public void setContent(String key, Object value) {
		if (content == null)
			content = new TreeMap<String, List<Object>>();
		List<Object> list = new ArrayList<Object>();
		if (content.get(key) != null) 				
			list = content.get(key);			
		list.add(value);
		content.put(key, list);		
	}

	public void setUnparsedContent(String unparsedContent) {
		this.unparsedContent = unparsedContent;
	}

	public String getUnparsedContent() {
		return unparsedContent;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	public void addSubPage(SubPage subPage) {
		subPageList.add(subPage);
	}
	
	public SubPage getSubPage(int index) {
		return subPageList.get(index);
	}
	
	public List<SubPage> getSubPageList() {
		return subPageList;
	}
	
	public void setSubPageList(List<SubPage> subPageList) {
		this.subPageList = subPageList;
	}

}
