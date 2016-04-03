package result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.nodes.Element;

public class Page {

	protected Map<String, List<Object>> content = new TreeMap<String, List<Object>>();

	protected String unparsedContent;

	protected String url;
	
	protected List<SubPage> subPageList = new ArrayList<SubPage>();
	
	private Element description;
	
	private Map<String, List<String>> source;

	public Page() {
		unparsedContent = "";
	}

	public Page(String unparsedContent) {
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
	
	public Element getDescription() {
		return description;
	}

	public void setDescription(Element description) {
		this.description = description;
	}

	/**
	 * @return the source
	 */
	public Map<String, List<String>> getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Map<String, List<String>> source) {
		this.source = source;
	}
	
	public void setSource(String key, String value) {
		if (source == null)
			source = new TreeMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		if (source.get(key) != null) 				
			list = source.get(key);			
		list.add(value);
		source.put(key, list);		
	}

}
