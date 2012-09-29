package UserInfo;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class ClientDB {
	private Connection connection;
	private Statement st;
	private ResultSet rs;
	private static final Logger log = Logger.getLogger(ClientDB.class.getName());
	
	public void initDB() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:Accounts.db");
		st = connection.createStatement();
		//  'role' enum('admin','user') ???
		st.execute("create table if not exists 'Accounts' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'username' text not null, 'password' text not null, 'role' text not null);");
		log.debug("Create table 'Account' if not exists");
	}
	
	public void addUser(String username, String password, String role) throws SQLException {
		st.execute("insert into 'Accounts' ('username', 'password', 'role') values ('" + username + "', '" + password + "', '" + role + "');" );
		log.debug("Create user '" + username + "'");
	}
	
	public boolean findUser(String username, String password) {
		boolean result = false;
		ClientAccount clientAccount = null;
		try {
			rs = st.executeQuery("select * from 'Accounts' where username='" + username + "' and password='" + password + "';");
			while (rs.next()){
				clientAccount = fetchAccount(rs);
				result = true;
				log.debug("Find user: " + clientAccount.getId() + ", " + clientAccount.getUsername());
			}
		} catch (SQLException e) {
			log.error("Not user in DB");
		}
		return result;
	}
	
	public boolean findUserRole(String username, String password, String role) {
		boolean result = false;
		ClientAccount clientAccount = null;
		try {
			rs = st.executeQuery("select * from 'Accounts' where username='" + username + "' and password='" + password + "' and role='" + role + "';");
			while (rs.next()){
				clientAccount = fetchAccount(rs);
				result = true;
				log.debug("Find user with role: " + clientAccount.getId() + ", " + clientAccount.getUsername()+ ", " + clientAccount.getRole());
			}
		} catch (SQLException e) {
			log.error("Not user in DB");
		}
		return result;
	}
	
	public List<ClientAccount> listOfUser() throws SQLException {
		List<ClientAccount> result = new LinkedList<ClientAccount>();
		rs = st.executeQuery("select * from 'Accounts';");
		while (rs.next()){
			ClientAccount client = fetchAccount(rs);
			result.add(client);
		}
		return result;
	}
	
	public ClientAccount fetchAccount(ResultSet rs) throws SQLException {
		ClientAccount clientAccount = new ClientAccount(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
		return clientAccount;
	}
	
	public void cleanDataDB() throws SQLException {
		st.execute("delete from 'Accounts' where id > 0 or id=0");
		log.debug("Data db clean");
	}
	
	public ResultSet listOfUserRS() throws SQLException {
		return rs = st.executeQuery("select * from 'Accounts';");
	}
}
