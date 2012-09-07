package vologzhanin; 
import vologzhanin.Controller;
import vologzhanin.Controller.Tourniquet;

// Пассажиры прибывают на платформу и регистрируются в контроллере с помощью турникетов
// прибытие пассажиров синхронизировано, пройти через турникет может только количество 
// пассажиров, вмещаемое тележкой. Остальные пассажиры ожидают в очереди
public class Passenger implements Runnable {
	Controller controller;
	Tourniquet tourniquet;
	public Passenger(Controller controller, Tourniquet tourniquet){
		this.controller = controller;
		this.tourniquet = tourniquet;
	}
	@Override
	public void run() { 
		System.out.println(Thread.currentThread().getName() + ": Passenger ARRIVE!" );
		try {
			while(tourniquet.arrivePassanger() == false);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + ": Passenger WENT through the tourniquet");
		try {
			while(tourniquet.outPassanger() == false);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + ": Passenger OUT through the tourniquet");
		}
}
	