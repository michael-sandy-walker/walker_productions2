package cookies;

public class SQLiteCookie {
	private int creation_utc;
	private String host_key;
	private String name;
	private String value;
	private String path;
	private int expires_utc;
	private int secure;
	private int httponly;
	private int last_access_utc;
	private int has_expires;
	private int persistent;
	private int  priority;
	private byte[] encrypted_value;
	private int firstpartyonly;
	public int getCreation_utc() {
		return creation_utc;
	}
	public void setCreation_utc(int creation_utc) {
		this.creation_utc = creation_utc;
	}
	public String getHost_key() {
		return host_key;
	}
	public void setHost_key(String host_key) {
		this.host_key = host_key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getExpires_utc() {
		return expires_utc;
	}
	public void setExpires_utc(int expires_utc) {
		this.expires_utc = expires_utc;
	}
	public int getSecure() {
		return secure;
	}
	public void setSecure(int secure) {
		this.secure = secure;
	}
	public int getHttponly() {
		return httponly;
	}
	public void setHttponly(int httponly) {
		this.httponly = httponly;
	}
	public int getLast_access_utc() {
		return last_access_utc;
	}
	public void setLast_access_utc(int last_access_utc) {
		this.last_access_utc = last_access_utc;
	}
	public int getHas_expires() {
		return has_expires;
	}
	public void setHas_expires(int has_expires) {
		this.has_expires = has_expires;
	}
	public int getPersistent() {
		return persistent;
	}
	public void setPersistent(int persistent) {
		this.persistent = persistent;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public byte[] getEncrypted_value() {
		return encrypted_value;
	}
	public void setEncrypted_value(byte[] encrypted_value) {
		this.encrypted_value = encrypted_value;
	}
	public int getFirstpartyonly() {
		return firstpartyonly;
	}
	public void setFirstpartyonly(int firstpartyonly) {
		this.firstpartyonly = firstpartyonly;
	}
}
