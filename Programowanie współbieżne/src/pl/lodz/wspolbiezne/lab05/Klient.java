package pl.lodz.wspolbiezne.lab05;

import java.util.logging.Logger;

public class Klient extends Thread {
	private Zas�b zas�b;
	private long pauza;
	private int znak�w;

	Klient(Zas�b b, long pauza, int znak�w) {
		this.zas�b = b;
		this.pauza = pauza;
		this.znak�w = znak�w;
		b.addTaskCount(znak�w);
	}

	public void run() {
		for (int i = 0; i < znak�w; i++) {
			zas�b.put();
			try {
				sleep(pauza);
			} catch (InterruptedException e) {
				Logger.getGlobal().info("Kto� przerwa� m�j sen!");
			}
		}
	}
}