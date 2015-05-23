package utilities;

public class UrlToken extends Token {

	public UrlToken(String content) {
		super(content);
	}
	
	public String getUrl() {
		return super.getContent();
	}

}
