package vologzhanin;
import java.util.Random;

import vologzhanin.Controller.Tourniquet;

public class RollerCoaster {
	static int timeMothionCarriage = 4000;
	static int maxPassangerInCarriage = 5; 
	static int numberPassenger = 12;
	
	public static void main(String[] args) throws InterruptedException {
		Random generatorTimeWaitPassanger = new Random();
		Controller controller = new Controller(timeMothionCarriage, maxPassangerInCarriage);
		Tourniquet tourniquet = controller.new  Tourniquet();
		Thread threadController = new Thread(controller); 
		threadController.start();
		System.out.println("\nRoller-Coaster start work");
		
		Passenger[] passengersMassive = new Passenger[numberPassenger];
		Thread[] threadsPassenger = new Thread [numberPassenger];
		for (int i = 0; i < numberPassenger; i++) {
			int timeSleep = generatorTimeWaitPassanger.nextInt(999);
			Thread.sleep(timeSleep);
			passengersMassive[i] = new Passenger(controller, tourniquet);
			threadsPassenger[i] = new Thread(passengersMassive[i]);
			threadsPassenger[i].start();
		}

		threadController.join();
		System.out.println("\nRoller-Coaster stop work");

		for (int i = 0; i < numberPassenger; i++) {
			threadsPassenger[i].join();
		}
	}
	}
