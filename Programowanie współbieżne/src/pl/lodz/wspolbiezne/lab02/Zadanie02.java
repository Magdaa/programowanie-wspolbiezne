package pl.lodz.wspolbiezne.lab02;

import java.util.Random;

public class Zadanie02 {
	
	private static int ROZMIAR_TABLICY = 4_000_000; 
	private W�tek[] threads;
	private byte[] tablicaBajtow;

	public static void main(String[] args) {

	}
	
	public Zadanie02(int threadsNumber) {
		tablicaBajtow = initTablicaBajtow();
		threads = initThreads(threadsNumber);
		startAll();
		
	}
	

	private W�tek[] initThreads(int threadsNumber) {
		W�tek[] thread = new W�tek[threadsNumber];
		for (int interval=0; interval<threadsNumber; interval++) {
			W�tek w = new W�tek();
			w.setStartIndex(getBeginningOfInterval(interval, threadsNumber));
			w.setEndIndex(getEndOfInterval(interval, threadsNumber));
			thread[interval] = w;
		}
		return thread;
	}
	
	private byte[] initTablicaBajtow() {
		byte[] tablicaBajtow = new byte[ROZMIAR_TABLICY];
		new Random().nextBytes(tablicaBajtow);
		return tablicaBajtow;
	}
	
	private Histogram getHistogram() {
		for (W�tek w : threads) {
			w.getHistogram();
		}
	}

	public static int getBeginningOfInterval(int interval, int totalIntervals) {
		if (totalIntervals<=interval) {
			throw new IllegalArgumentException("Przedzia� nie mo�e by� wi�kszy ni�: "
					+totalIntervals+" a podano: "+interval);
		}
		double fraction = (double)interval/(double)totalIntervals;
		return (int) (fraction * ROZMIAR_TABLICY);
	}
	
	public static int getEndOfInterval(int interval, int totalIntervals) {
		double rozmiarPrzedzialu = (double)ROZMIAR_TABLICY/(double)totalIntervals;
		double fraction = (double)interval/(double)totalIntervals; 
		return (int) ((fraction * ROZMIAR_TABLICY) + rozmiarPrzedzialu);
	}
	
	public void startAll() {
		for (W�tek w : threads) {
			Thread t = new Thread(w);
			t.start(); 
		}
	}
}
