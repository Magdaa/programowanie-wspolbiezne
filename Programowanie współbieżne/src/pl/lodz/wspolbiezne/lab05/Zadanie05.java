package pl.lodz.wspolbiezne.lab05;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Zadanie05 {
	public static void main(String[] args) {
		Logger.getGlobal().setLevel(Level.FINE);
		Zas�b b = new Zas�b(4);
		Klient p = new Klient(b);
		Serwer c = new Serwer(b);

		p.start();
		c.start();
	}
}