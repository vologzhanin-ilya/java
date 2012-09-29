package Server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import Client.ClientController;
import Client.ClientLoginFrame;
import Client.InfoMessage;
import Message.Message;

public class Server {
	private static final Logger log = Logger.getLogger(Server.class.getName());
	//список, где хранятся все клиенты для рассылки
	public static List<ClientController> chatClients = new LinkedList<ClientController>(); 
	private InfoMessage infoMessage;
	public List<ClientController> getChatClients() {
		for (int i = 0; i < chatClients.size(); i++) 
			System.out.println("*** " + chatClients.get(i).getUsername());
		return chatClients;
	}

	private static Lock clientLock = new ReentrantLock();						
	private static int nClients;
	public static ServerSocket serverSocket;
	public ClientLoginFrame clf;
	
	public synchronized ServerSocket startServer(int port) throws IOException {
		// открываем сокет
		ServerSocket serverSocket = new ServerSocket(port);
      	// Выводим пользователю сообщение о возможности запуска
      	infoMessage = new InfoMessage(250, 110, "     Server start work!", "Start server", "./image/Ok-icon.png");
      	infoMessage.setVisible(true);
      	log.debug("Server start work");
		return serverSocket;
	}
	
	public synchronized boolean shutdownServer(){
		boolean shutdown = false;
		// обрабатываем список рабочих коннектов, закрываем каждый
		for (ClientController clController : chatClients) {
			clController.close();
			shutdown = true;
		}
		if (!serverSocket.isClosed()) {
			try {
				serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
					}
			}
		infoMessage = new InfoMessage(250, 110, "Server stop work", "Stop server", "er.png");
		infoMessage.setVisible(true);
		return shutdown;
		}

	public static void messageExchange(ClientController clientController) throws IOException, ClassNotFoundException {
		// получаем сообщение
		Message message = clientController.getMessage();	
		
		if(message.getType() == Message.CONNECT) {		
			clientController.setUsername(message.getUser());	
			clientLock.lock();
			try {
				chatClients.add(clientController);
				
				log.info("List online users:");
				for (int i = 0; i < chatClients.size(); i++) 
					log.info("*** " + chatClients.get(i).getUsername());
				
				
				for (ClientController clController : chatClients) {
					clController.sendMessage(new Message(Message.MESSAGING, clientController.getUsername(), clientController.getUsername() + " online"));
					System.out.println("++ User");
					System.out.println(clController.getUsername());
				}
			} finally {
				clientLock.unlock();
			}
		} else {									
			return;
		}
		
		while(true) {
			message = clientController.getMessage();
			switch(message.getType()) {
				case Message.DISCONNECT:	
					try {
						chatClients.remove(clientController);
						for (ClientController clController : chatClients) 
							clController.sendMessage(new Message(Message.MESSAGING, clientController.getUsername(), clientController.getUsername() + " offline"));
					} finally {
						clientLock.unlock();
					}
					return;
				
				case Message.MESSAGING:
					clientLock.lock();
					try {
						for (ClientController clController : chatClients)
							clController.sendMessage(message);
								
						} finally {
							clientLock.unlock();
						}
					break;
			}
		}
	
	}
	
	public static void main(String[] args) throws IOException {
		int port = 9999;
		final Server server = new Server();
		serverSocket = server.startServer(port);
		nClients = 0;
		
		/*
	    * главный цикл прослушивания/ожидания коннекта.
	    */
		while(true) {
			// получить новое соединение
			final Socket clientSocket = serverSocket.accept();
			final ClientController clientController = new ClientController(clientSocket);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						nClients++;
						log.debug("Client connected. Number clients on server: " + nClients);
						messageExchange(clientController);
	            		} catch (EOFException e) {
	            		} catch (IOException e) {
	            		} catch (ClassNotFoundException e) {
	            		} finally {
	            			nClients--;
	            			log.debug("Client disconnected. Number clients on server: " + nClients);
	            		}
	            	}
				}).start();
			}
	}
}
	

