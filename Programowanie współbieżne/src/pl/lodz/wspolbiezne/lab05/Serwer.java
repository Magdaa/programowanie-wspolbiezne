package pl.lodz.wspolbiezne.lab05;

class Serwer extends Thread {
	private Zas�b zas�b;

	Serwer(Zas�b b) {
		zas�b = b;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			zas�b.take();
		}
	}
}