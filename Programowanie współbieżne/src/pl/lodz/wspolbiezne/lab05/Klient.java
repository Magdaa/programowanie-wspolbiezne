package pl.lodz.wspolbiezne.lab05;

public class Klient extends Thread {
	private Zas�b zas�b;

	Klient(Zas�b b) {
		zas�b = b;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			zas�b.put((char) ('A' + i % 26));
		}
	}
}