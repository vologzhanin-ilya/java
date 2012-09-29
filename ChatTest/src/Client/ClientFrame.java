package Client;

//Форма чат-клиента
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import Server.*;

@SuppressWarnings("serial")
class ClientFrame extends JFrame {
	private static final Logger log = Logger.getLogger(ClientFrame.class.getName());
		ServerController sController;
		Font font = new Font("Verdana", Font.PLAIN, 12);
		
		public JTextArea textPanelChat;
	    public JTextField textMessageField;
	    public java.awt.List listContact;
	    
	    JLabel labelServerIP;
	    JLabel onlineUser;
	    JTextField serverIPField;
	    String username;
		String password;
		ImageIcon iconChat = new ImageIcon("./image/p.png");
		Socket serverSocket;
		ClientLoginFrame clf;
		private InfoMessage infoMessage;
		
		public ClientFrame() throws ParseException {
			mainWindow(630, 350);
			otherWindow();
		}
		
		public ClientFrame(int weight, int height) throws ParseException {
			mainWindow(weight, height);
		}
		private static ServerController servController() {
			return new ServerController();
		}
		
		private void mainWindow(int width, int height) throws ParseException {
			sController = servController();
			setTitle("Chat client");
			setSize(width,height);
			setResizable(false);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setBounds(((screenSize.width - width)/2), ((screenSize.height - height)/2), width, height);
			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			this.setLayout(null);
			setIconImage(iconChat.getImage());
    	 
			textPanelChat = new JTextArea();
			textPanelChat.setBounds(10, 28, 480, 250);
			getContentPane().add(textPanelChat);
			
			textPanelChat.setEditable(false);
			
//			JScrollPane scrollPane = new JScrollPane(textPanelChat);
//			add(scrollPane, BorderLayout.CENTER);
			
			textMessageField = new JTextField();
			textMessageField.setBounds(10, 290, 480, 25);
			getContentPane().add(textMessageField);

		}
		
		public void otherWindow(){			
			
			listContact = new java.awt.List();
			listContact.setBounds(500, 110, 115, 168);
			getContentPane().add(listContact);
    	 
			listContact.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					String privateUserMessage = listContact.getSelectedItem();
					try {
						ClientFrame cf = new ClientFrame(505, 350);
						cf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						cf.setVisible(true);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				}
			});
			
			onlineUser = new JLabel();
			onlineUser.setText("Online users");
			onlineUser.setBounds(520, 90, 115, 20);
			onlineUser.setFont(font);
			getContentPane().add(onlineUser);
			
			labelServerIP = new JLabel();
			labelServerIP.setText("Server IP :");
			labelServerIP.setBounds(430, 5, 200, 20);
			labelServerIP.setFont(font);
	        getContentPane().add(labelServerIP);
	        
//			MaskFormatter mf = new MaskFormatter("###.###.###.###");
//	   	  	mf.setPlaceholderCharacter(' ');
//			JFormattedTextField jServerIPField = new JFormattedTextField(mf);
			serverIPField = new JTextField();
			serverIPField.setBounds(500, 5, 115, 20);
			getContentPane().add(serverIPField);
	     
			//-------------------------------------------------------
			// Button
			JButton buttonConnect = new JButton();
			buttonConnect.setBounds(500, 30, 115, 25);
			buttonConnect.setText("Connect");
			buttonConnect.setFont(font);
      
			buttonConnect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					actionConnect(evt, sController);
				}
			});
			serverIPField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					actionConnect(evt, sController);
				}
			});
			this.getContentPane().add(buttonConnect);
    	 
			JButton buttonDisconnect = new JButton();
			buttonDisconnect.setBounds(500, 60, 115, 25);
			buttonDisconnect.setText("Disconnect");
			buttonDisconnect.setFont(font);
    	 
			buttonDisconnect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					actionDisconnect(evt, sController);
            	 }
    		 });
			this.getContentPane().add(buttonDisconnect);
    	 
			JButton buttonSend = new JButton();
			buttonSend.setBounds(500, 290, 115, 25);
			buttonSend.setText("Send");
			buttonSend.setFont(font);
    	 
			buttonSend.addActionListener(new ActionListener() {  
				@Override
				public void actionPerformed(ActionEvent evt) {
					actionSend(evt, sController);
				}
			});  	
			
			textMessageField.addActionListener(new ActionListener() {  
				@Override
				public void actionPerformed(ActionEvent evt) {
					actionSend(evt, sController);
				}
			});  	
			
			this.getContentPane().add(buttonSend);
			
			
			new Thread(new Runnable() {							
				@Override
			   public void run() {
		        	String tempMessage = null;
		          	 while (true) {
		          		if( ServerController.isListening() ) {	
		                   tempMessage = sController.receive(); // читаем строку от пользователя
		          			if( tempMessage != null )					
		          				textPanelChat.append( tempMessage + "\n" );
		          		}
//		                   if (tempMessage == null || tempMessage.length() == 0 || serverSocket.isClosed()) {
//		                	   log.error("Close connection from server");
//		                      close(); 
//		                      } else {
//		                    try {
////		                    } catch (IOException e) {
////		                      close();
////		                    }
//		                   }
		          	 }
				}
				}).start();
			}

		@SuppressWarnings("static-access")
		private void actionConnect(ActionEvent evt, ServerController sController) {
			log.debug("Connect new user: " + username);
				if( !(sController.isConnecting()) ) {					
					sController.setServerIP(serverIPField.getText());
					log.debug("set IP-address from server: '" + serverIPField.getText() + "' from user: " + username);
					
					if( sController.connect(username) )	{
						textMessageField.requestFocusInWindow();
					}
					
					else {
				        infoMessage = new InfoMessage(320, 110, "  Unable to connect. Server not running?", "ERROR", "./image/er1.png");
				        infoMessage.setVisible(true);
						textPanelChat.append("Can't connect to server with user: " + username + "\n");	
						log.error("Can't connect to server with user: " + username);
					}
				} 
				
			}
     
		private void actionDisconnect(ActionEvent evt, ServerController sController) {
			log.debug("Disonnect user: " + username);
				if( sController.disconnect() ) {
					textPanelChat.append("You leaved the chat" + "\n");
					log.debug("Close connection from server - client leaved the chat");
				}
				else {
					textPanelChat.append("User disconnected from server" + "\n");
					log.error("User disconnect from server");
				}
		}
     
		@SuppressWarnings("static-access")
		private void actionSend(java.awt.event.ActionEvent evt, ServerController sController) {
			if( sController.isConnecting() ) {						
    			if( sController.sendMessage(textMessageField.getText()) )	
    				// Очищаем поле сообщения после отправки
    				textMessageField.setText(null);	
    			else {
    				textPanelChat.append("User disconnected from server" + "\n");	
    				log.error("User disconnect from server");
    			}
    		} else {	
    			textPanelChat.append("User don't joined to the chat" + "\n");
    			log.error("Don't connection from chat");
    		}
		}
	}
