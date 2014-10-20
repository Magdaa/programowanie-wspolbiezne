package pl.lodz.wspolbiezne.tutorial;

public class Test01 {

	public static void main(String[] args) {

		MySignal mySignal = new MySignal();
		
		MyThread a = new MyThread(mySignal);
		a.start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mySignal.setHasDataToProcess(true);
		
	}
}
