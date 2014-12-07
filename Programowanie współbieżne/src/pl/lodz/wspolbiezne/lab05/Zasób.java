package pl.lodz.wspolbiezne.lab05;

import java.util.logging.Logger;

public class Zas�b {
	private char[] zas�b;
	private int count = 0, in = 0, out = 0;

	public Zas�b(int size) {
		zas�b = new char[size];
	}

	public synchronized void put(char c) {
		while (count == zas�b.length) {
			Logger.getGlobal().info("Zas�b jest pe�ny, wi�c usypiam w�tek.");
			try {
				wait();
			} catch (InterruptedException e) {
				Logger.getGlobal().info("Nast�pi�o przerwanie w metodzie put");
			}
		}
		Logger.getGlobal().info("-->> " + c + " -->>");
		zas�b[in] = c;
		in = (in + 1) % zas�b.length;
		count++;
		notifyAll();
	}

	public synchronized char take() {
		while (count == 0) {
			Logger.getGlobal().fine("Zas�b jest pusty, wi�c usypiam w�tek.");
			try {
				wait();
			} catch (InterruptedException e) {
				Logger.getGlobal().info("Nast�pi�o przerwanie w metodzie take");
			} 
		}
		char c = zas�b[out];
		out = (out + 1) % zas�b.length;
		count--;
		Logger.getGlobal().info("<<-- " + c + " <<--");
		notifyAll();
		return c;
	}
}