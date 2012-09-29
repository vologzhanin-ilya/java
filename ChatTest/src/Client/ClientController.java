/**************************************************************************
 * PROJECT: Lab4
 * DESCRIPTION:
 * ���������� ������������� ����
 * AUTHOR: Ponomaryova Nadya
 *************************************************************************/
package Client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import Message.Message;

public class ClientController {
	private static final Logger log = Logger.getLogger(ClientController.class.getName());
	
	private String username;	
	private Socket socket;				
	private ObjectInputStream inputStream;		// читатель с сервера
	private ObjectOutputStream outputStream;	// писатель на сервер	
	private Lock inputLock = new ReentrantLock();				
	private Lock outputLock = new ReentrantLock();		
	
	
	/**
	 * Конструктор объекта клиента
	 * @param sock - сокет
	 * @throws java.io.IOException - если не смогли приконнектиться, кидается исключение, чтобы
	 * предотвратить создание объекта
	 */
	public ClientController(Socket sock) throws IOException, EOFException {
		socket = sock;
		// создаем читателя и писателя в сокет
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());
	}
	
	public ClientController(){
	}

	public void sendMessage(Message message) throws IOException {
		outputLock.lock();
		try {
			if (message.getText() == null) {  // строка будет null если сервер прикрыл коннект по своей инициативе, сеть работает
              log.error("Server has closed connection");
              close(); // ...закрываемся
          } else { 
        	  outputStream.writeObject(message);
          }
		} finally {
			outputLock.unlock();
		}
	}
	
	public Message getMessage() {
		Message message = null;
		inputLock.lock();
		try {
			try {
				message = (Message) inputStream.readObject();
			} catch (IOException e) { // если в момент чтения ошибка
				// проверим, что это не банальное штатное закрытие сокета сервером
				if ("Socket closed".equals(e.getMessage())) {
					log.error("Socket close!");
				}
				// в случае ошибок сети.
				log.error("Connection lost"); 
				// закрываем сокет
				close(); 
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} finally {
			inputLock.unlock();
		}
		return message;
	}
	 
	/*
	 * метод закрывает коннект и выходит из программы
	 */
	public synchronized void close() {//метод синхронизирован, чтобы исключить двойное закрытие
		if (!socket.isClosed()) { // проверяем, что сокет не закрыт
			try {
				socket.close(); // закрываем
				System.exit(0); // выходим!
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}