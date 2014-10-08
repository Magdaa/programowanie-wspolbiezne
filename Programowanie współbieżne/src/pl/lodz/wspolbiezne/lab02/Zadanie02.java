package pl.lodz.wspolbiezne.lab02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Zadanie02 {

	private static int ROZMIAR_TABLICY = 4_000_000;
	private W�tek[] threads;
	private byte[] tablicaBajtow;
	private int[] histogram;

	public static void main(String[] args) {
		new Zadanie02(1);
		// new Zadanie02(2);
		// new Zadanie02(4);
		// new Zadanie02(6);
		// new Zadanie02(8);
		// new Zadanie02(10);
		// new Zadanie02(12);
		// new Zadanie02(14);
		// new Zadanie02(160);
	}

	public Zadanie02(int threadsNumber) {
		long startTime = System.currentTimeMillis();
		histogram = new int[256];
		tablicaBajtow = initTablicaBajtow();
		threads = initThreads(threadsNumber);
		startAll();
		System.out.println(Arrays.toString(histogram));
		System.out.println("Liczba bajt�w: " + IntStream.of(histogram).sum());
		long endTime = System.currentTimeMillis();
		System.out.println("Czas wykonania: " + (endTime - startTime) + "ms");
	}

	private W�tek[] initThreads(int threadsNumber) {
		if (threadsNumber < 0) {
			throw new IllegalArgumentException(
					"Liczb� w�tk�w musi by� wi�ksza od 0");
		}
		W�tek[] thread = new W�tek[threadsNumber];
		for (int interval = 0; interval < threadsNumber; interval++) {
			W�tek w = new W�tek();
			w.setStartIndex(getBeginningOfInterval(interval, threadsNumber));
			w.setEndIndex(getEndOfInterval(interval, threadsNumber));
			w.setTablicaBajtow(tablicaBajtow);
			w.setHistogram(histogram);
			thread[interval] = w;
		}
		return thread;
	}

	private byte[] initTablicaBajtow() {
		Path path = Paths.get("resources/bajty.dat");
		byte[] bajty = null;
		try {
			if (!Files.exists(path)) {
				bajty = new byte[ROZMIAR_TABLICY];
				new Random().nextBytes(bajty);
				Files.write(path, bajty);
			} else {
				bajty = Files.readAllBytes(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bajty;
	}

	public static int getBeginningOfInterval(int interval, int totalIntervals) {
		if (totalIntervals <= interval) {
			throw new IllegalArgumentException(
					"Przedzia� nie mo�e by� wi�kszy ni�: " + totalIntervals
							+ " a podano: " + interval);
		}
		double fraction = (double) interval / (double) totalIntervals;
		return (int) (fraction * ROZMIAR_TABLICY);
	}

	public static int getEndOfInterval(int interval, int totalIntervals) {
		double rozmiarPrzedzialu = (double) ROZMIAR_TABLICY
				/ (double) totalIntervals;
		double fraction = (double) interval / (double) totalIntervals;
		return (int) ((fraction * ROZMIAR_TABLICY) + rozmiarPrzedzialu);
	}

	public void startAll() {
		int i = 0;
		for (W�tek w : threads) {
			Thread t = new Thread(w);
			t.start();
			System.out.println("W�tek " + ++i + " uruchmiony");
		}
	}
}
