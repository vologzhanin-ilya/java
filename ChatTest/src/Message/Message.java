package Message;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable {
	private String user;
	private String text;
	private int type;
	
	public static final int CONNECT = 0;
	public static final int DISCONNECT = 1;
	public static final int MESSAGING = 2;
	public static final int PRIVATE = 3;
	
	public Message(int type, String sender, String text) {
		this.setType(type);
		this.setUser(sender);
		this.setText(text);
	}

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
