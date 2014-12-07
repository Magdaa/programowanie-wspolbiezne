package pl.lodz.wspolbiezne.lab05;

import java.util.logging.Logger;

class Serwer extends Thread {
	private Zas�b zas�b;

	Serwer(Zas�b b) {
		zas�b = b;
	}

	public void run() {
		while (!zas�b.tasksFinished()) {
			zas�b.take();
		}
		Logger.getGlobal().info("Zako�czono procesowanie wszystkich zleconych zada�.");
	}
}