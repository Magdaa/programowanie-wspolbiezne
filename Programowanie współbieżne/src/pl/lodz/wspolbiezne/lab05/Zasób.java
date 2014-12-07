package pl.lodz.wspolbiezne.lab05;

import java.util.logging.Logger;

/**
 * To jest de facto Monitor zawieraj�cy zas�b (tablic� znak�w).
 * Ten obiekt jest przekazywany do konstruktora w�tku i przekazywany
 * z "r�k do r�k".
 * 
 * @author �ukasz Ochma�ski, Marcel Wieczorek
 *
 */
public class Zas�b {
	private char[] zas�b;
	private int count, in, out, literka;
	private static int tasks;

	public Zas�b(int size) {
		zas�b = new char[size];
	}

	public synchronized void put() {
		while (count == zas�b.length) {
			Logger.getGlobal().info("Zas�b jest pe�ny, wi�c usypiam w�tek.");
			try {
				wait();
			} catch (InterruptedException e) {
				Logger.getGlobal().info("Nast�pi�o przerwanie w metodzie put");
			}
		}
		char c =(char) ('A' + literka % 26);
		Logger.getGlobal().info("-->> " + c + " -->>");
		zas�b[in] = c;
		in = (in + 1) % zas�b.length;
		literka++;
		count++;
		notifyAll();
	}

	public synchronized char take() {
		while (count == 0) {
			Logger.getGlobal().info("Zas�b jest pusty, wi�c usypiam w�tek.");
			try {
				wait();
			} catch (InterruptedException e) {
				Logger.getGlobal().info("Nast�pi�o przerwanie w metodzie take");
			} 
		}
		char c = zas�b[out];
		out = (out + 1) % zas�b.length;
		count--;
		tasks--;
		Logger.getGlobal().info("<<-- " + c + " <<--");
		notifyAll();
		return c;
	}
	
	public synchronized boolean tasksFinished() {
		return tasks==0;
	}

	public synchronized void addTaskCount(int znak�w) {
		tasks +=znak�w;
	}
}