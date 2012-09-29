package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import config.Config;
import Message.Message;

public class ServerController {
	
	static private String serverIP;			
	static private int serverPort;			
	static private Socket clientSocket;			
	static private ObjectOutputStream outputStream;	
	static private ObjectInputStream inputStream;	
	static private String username;			
	static private boolean isConnect;			
	static private boolean isListen;		
	private static final Logger log = Logger.getLogger(ServerController.class.getName());
	public String configFile = "./configuration/config.properties";
	public Config config;
	//	public int port = 9999;
	
	public ServerController() {
		// по умолчанию - подключение к локальному хосту (если не введено значение поля ip сервера
		serverIP = "localhost";
		
		try {
			config = new Config(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		serverPort = Integer.parseInt(config.getPort());
		isConnect = false;
		isListen = false;
	}
	
	public String setTime() {
		long time = System.currentTimeMillis(); 
	  	SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		return format.format(new Date(time));
	}
	
	
	/*
	 * Ожидает новое подключение.
	 */
	public boolean connect(String username) {
		if( !isConnecting() ) {
			ServerController.username = username;
			clientSocket = new Socket();				
			try {
				try {
					clientSocket.connect(new InetSocketAddress(serverIP, serverPort));
				} catch (IOException e) {
					// если ошибка в момент приема - останавливаем работу сервера
//		            server.shutdownServer(); 
				}
				
				outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				inputStream = new ObjectInputStream(clientSocket.getInputStream());	
				setListening(true);
				outputStream.writeObject(new Message(Message.CONNECT, username, "[" + setTime() + "]: " + username + " join"));
			} catch (IOException e) {
				setListening(false);
				return false;
			}	
			return true;
		}
		return false;
	}
	
	

	public boolean disconnect() {
		if( isConnecting() ) {
			setConnecting(false);
			setListening(false);
			if (!clientSocket.isClosed()) {
				try {
					outputStream.writeObject(new Message(Message.CONNECT, username, "[" + setTime() + "]: " + username + " leave"));
					clientSocket.close();
				} catch (IOException e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public String receive() {
		if( isListening() ){
			Message msg = null;
			try {
				msg = (Message) inputStream.readObject();	
			} catch (IOException e) {				
				if( isListening() ) {
					setConnecting(false);
					setListening(false);
					return "User disconnected from server";
				} else
					return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if( !isConnecting() ) {
				if( msg.getType() == Message.MESSAGING  )
					setConnecting(true);
			}
			return "[" + setTime() + "] <" + msg.getUser() + ">: " + msg.getText();
		}
		return null;
	}
	
	/*
     * Метод посылает в сокет полученную строку
     */
	public boolean sendMessage(String line) {
		if( isConnecting() ) {
			Message msg = new Message(Message.MESSAGING, username, line);
			try {
				outputStream.writeObject(msg);
			} catch (IOException e) {	//если проблема в момент отправки
				setConnecting(false);
				setListening(false);
				log.error("Message don't send!");
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static void setServerIP(String serverIP) {
		ServerController.serverIP = serverIP;
	}
	
	public static synchronized boolean isListening() {
		return isListen;
	}
	public static synchronized boolean isConnecting() {
		return isConnect;
	}
	private static synchronized void setListening(boolean listen) {
		isListen = listen;
	}
	private static synchronized void setConnecting(boolean connect) {
		isConnect = connect;
	}
}
