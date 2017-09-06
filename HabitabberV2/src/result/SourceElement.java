package result;

import java.util.LinkedHashMap;
import java.util.Map;

public class SourceElement {
	private String url;
	private String title;
	private String alt;
	
	private Map<String, String> otherAttributes = new LinkedHashMap<>();
	
	public SourceElement() {
		
	}
	
	public SourceElement(String url, String title, String alt) {
		this.url = url;
		this.title = title;
		this.alt = alt;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the alt
	 */
	public String getAlt() {
		return alt;
	}
	/**
	 * @param alt the alt to set
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * @return the otherAttributes
	 */
	public Map<String, String> getOtherAttributes() {
		return otherAttributes;
	}

	/**
	 * @param otherAttributes the otherAttributes to set
	 */
	public void setOtherAttributes(Map<String, String> otherAttributes) {
		this.otherAttributes = otherAttributes;
	}
}
