package Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.log4j.Logger;

import UserInfo.Administrator;
import UserInfo.ClientDB;

@SuppressWarnings("serial")
public class ClientLoginFrame extends javax.swing.JFrame{
	private static final Logger log = Logger.getLogger(ClientLoginFrame.class.getName());
	public JTextPane jTextPane = new JTextPane();
	public JTextField usernameField;
	public JPasswordField passwordField;
	public JTextField jServerIPField;
	ClientFrame clientFrame;
	InfoMessage infoMessage;
	ClientDB clientDB = new ClientDB();
	public int WIDTH;
	public int HEIGHT;
	
	public String username;
	Font font = new Font("Verdana", Font.PLAIN, 12);
	ImageIcon iconChat = new ImageIcon("./image/p.png");
	public ClientLoginFrame(String role) {
			try {
				clientDB.initDB();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (role == "user") {
				authorizationWindow(320,150, "Authorization client");
				buttonLogin(40, 80, 100, 28, role);
				buttonRegistration();
			}
			else {
				authorizationWindow(320,150, "Interface for administrator");
				buttonLogin(90, 80, 130, 28, role);
			}
	    }
	
	    
	    private void authorizationWindow(int width, int height, String title) {
	    	setTitle(title);
	    	setResizable(false);
	    	setDefaultCloseOperation(EXIT_ON_CLOSE);
	    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    	setBounds(((screenSize.width - width)/2), ((screenSize.height - height)/2), width, height);
	    	setIconImage(iconChat.getImage());
	    	
	    	this.setLayout(null);
	        
	        
	        JLabel labelUsername = new JLabel("Username");
	        labelUsername.setFont(font);
	        labelUsername.setBounds(20, 11, 200, 20);
	        getContentPane().add(labelUsername);
	        
	        JLabel labelPassword = new JLabel();		
	        labelPassword.setText("Password");
	        labelPassword.setBounds(20, 44, 200, 20);
	        labelPassword.setFont(font);
	        getContentPane().add(labelPassword);
	        
	        usernameField = new JTextField();
	        usernameField.setBounds(100, 11, 180, 20);
	        getContentPane().add(usernameField);
	        
	        passwordField = new JPasswordField();       
	        passwordField.setBounds(100, 44, 180, 20);
	        getContentPane().add(passwordField);
	}

	public void buttonLogin(int x1, int y1, int x2, int y2, final String role) {
		JButton buttonLogin = new JButton();
        buttonLogin.setBounds(x1, y1, x2, y2);
        buttonLogin.setText("Login");
        buttonLogin.setFont(font);
        
        buttonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
					try {
						ActionLogin(evt, role);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
            }
        });
        this.getContentPane().add(buttonLogin);
        getRootPane().setDefaultButton(buttonLogin);
	}   
	
	public void buttonRegistration() {
		JButton buttonReg = new JButton();
        buttonReg.setBounds(160, 80, 120, 28);
        buttonReg.setText("Registration");
        buttonReg.setFont(font);
        buttonReg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				try {
					actionRegistration(evt);
				} catch (ParseException e) {
					e.printStackTrace();
				}
            }
        });
        this.getContentPane().add(buttonReg);
	}

	@SuppressWarnings("deprecation")
	private void ActionLogin(ActionEvent evt, String role) throws ParseException, SQLException {
		log.info("Authorizate user: " + username);
		String username = usernameField.getText();
		String password = passwordField.getText();
		
		if (clientDB.findUserRole(username, password, role) == false) {
			log.debug("Invalid login/password: " + username);
			infoMessage = new InfoMessage(250, 110, "   Invalid login/password", "ERROR", "./image/e.png");
			infoMessage.setVisible(true);
		}
		else {
			if (role == "user") {
				clientFrame = new ClientFrame();
				clientFrame.username = username;
				clientFrame.password = password;
				clientFrame.setVisible(true);
				this.dispose();
			}
			else {
	            ResultSet rs = clientDB.listOfUserRS();
	            Administrator model = new Administrator();
	            try {
					model.adminFrame(model, rs);
					this.dispose();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void actionRegistration(ActionEvent evt) throws ParseException{
		String username = usernameField.getText();
		String password = passwordField.getText();
		try {
			if (clientDB.findUser(username, password)) {
				infoMessage = new InfoMessage(250, 110, "  User already exists", "Registration", "./image/e.png");
				infoMessage.setVisible(true);
				log.debug("User already exists: " + username);
			}
			else {
				if (usernameField.getText().trim().length() > 0  && passwordField.getText().trim().length() > 0) {
					clientDB.addUser(username, password, "user");
					clientFrame = new ClientFrame();
					clientFrame.username = username;
					clientFrame.password = password;
					clientFrame.setVisible(true);	
					infoMessage = new InfoMessage(250, 110, "  Registration successful", "Registration", "./image/add_.png");
					infoMessage.setVisible(true);
					this.dispose();
					log.debug("Registration user: " + username + " successfull");
				}
				else {
					infoMessage = new InfoMessage(250, 110, "  All fields must be filled", "Registration", "./image/e.png");
					infoMessage.setVisible(true);
					log.debug("All fields must be filled");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
         java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
        	 new ClientLoginFrame("user").setVisible(true);
        	 }
         });
    }
}
