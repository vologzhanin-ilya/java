package UserInfo;

public class ClientAccount {
	private int id;
	private String username;
	private String password;
	private String role;

	
	public ClientAccount(int id, String username, String password, String role) {
		this.setId(id);
		this.setUsername(username);
		this.setPassword(password);
		this.setRole(role);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
