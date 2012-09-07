package vologzhanin;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
 * КЛАСС: Контроллер (система управления)
 * Контроллер и тележка взаимодействуют с помощью действия отправления тележки.
 * Турникет и контроллер взаимодействуют с помощью общего действия, обозначающего прибытие пассажира.
 * Контроллер определяет время движение тележки, а также максимально допустимую вместимость
 */
public class Controller implements Runnable {
	public int wait = 1000;
	private int state = 10;
	private int countPassangerInCarriage = 0;
	private int mothionTime;
	private int maxPass;
	// значение состояний системы
	public final int PASSENGER_ARRIVE = 0;		// состояние: прибытие пассажира 
	public final int PASSENGER_OUT = 1;		
	public final int CARRIAGE_MOVE = 2;		// сотсояние: движение тележки
	// Реентабельные блокировки
	private Lock lockForCountPassenger = new ReentrantLock();
	private Lock lockForStateSystem = new ReentrantLock();
	private boolean waitNewPassanger;	
	Carriage carriage = new Carriage();

	public Controller(int mothionTime, int maxPass){
		this.mothionTime = mothionTime;
		this.maxPass = maxPass;
	}
	
	@Override
	public void run() {
		while(true) {
			lockForStateSystem.lock();
			try {
				state = PASSENGER_ARRIVE;
			} finally { lockForStateSystem.unlock(); }
			// После прибытия заданного количества пассажиров счетчик сбрасывается
			System.out.println("\n Skip passengers suspended! Passengers can take the carriage");
			waitNewPassanger = true;

			while(waitNewPassanger) {
				lockForCountPassenger.lock();
				try {
					if( countPassangerInCarriage == maxPass ) waitNewPassanger = false; 
				} finally { lockForCountPassenger.unlock(); }
				
				if(!waitNewPassanger) break; 					
			}
			try {
				carriage.moveCarriage();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * КЛАСС: Турникет
	 * Турникет и контроллер взаимодействуют с помощью общего действия, обозначающего прибытие пассажира.
	 */
	public class Tourniquet {
		  public synchronized boolean arrivePassanger() throws InterruptedException {
		    	boolean result = true;
		    	lockForStateSystem.lock();
		    	lockForCountPassenger.lock();
				try {
					if( state == PASSENGER_ARRIVE ) { 							
			    		if( countPassangerInCarriage < maxPass ) countPassangerInCarriage++;
						else result = false;
			    	} else result = false;
				} finally {
					lockForCountPassenger.unlock();
					lockForStateSystem.unlock();
				}
				return result;
		    }
		    public synchronized boolean outPassanger() throws InterruptedException {
		    	boolean res = true;
		    	lockForStateSystem.lock();
		    	lockForCountPassenger.lock();
				try {
					if( state == PASSENGER_OUT ) { 
			    		if( countPassangerInCarriage > 0 )	countPassangerInCarriage--;		
						else res = false;
			    	} else res = false;
				} finally {
					lockForCountPassenger.unlock();
					lockForStateSystem.unlock();
				}
				return res;
				}
		    }
	/*
	 * КЛАСС: Тележка
	 * Контроллер и тележка взаимодействуют с помощью действия отправления тележки.
	 */
	public class Carriage {
		public void moveCarriage() throws InterruptedException {
			lockForStateSystem.lock();
			try {
				state = CARRIAGE_MOVE;
			} finally {
				lockForStateSystem.unlock();
			}
			System.out.println("\n|__|--> Carriage went!");
			Thread.sleep(mothionTime);
			System.out.println("\n|__|<-- Carriage arrive!");
			lockForStateSystem.lock();
			try {
				state = PASSENGER_OUT;
			} finally {
				lockForStateSystem.unlock();
			}
			Thread.sleep(wait);
			}
		}
	}
