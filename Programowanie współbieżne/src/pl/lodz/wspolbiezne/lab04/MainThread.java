package pl.lodz.wspolbiezne.lab04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class MainThread {

	private static final String SCIEZKA_DO_PLIKU = "resources/bajty.dat";
	private static final int ROZMIAR_TABLICY = 4_000_000;
	private static final boolean DEBUG = false;
	private long startTime;
	private Thread[] threads;
	private byte[] tablicaBajtow;
	private int[] histogram;
	private int liczbaW�tk�wNaPocz�tku;
	private int threadCounter;

	public MainThread(int threadsNumber) {
		liczbaW�tk�wNaPocz�tku = threadsNumber;
		setThreadCounter(threadsNumber);
		setStartTime(System.currentTimeMillis());
		histogram = new int[256];
		setTablicaBajtow(initTablicaBajtow());
		threads = initThreads(threadsNumber);
		startAll();
	}

	private Thread[] initThreads(int threadsNumber) {
		if (threadsNumber < 1) {
			throw new IllegalArgumentException(
					"Liczb� w�tk�w musi by� wi�ksza od 0");
		}
		Thread[] thread = new Thread[threadsNumber];
		for (int interval = 0; interval < threadsNumber; interval++) {
			W�tek w = new W�tek();
			w.setStartIndex(getBeginningOfInterval(interval, threadsNumber));
			w.setEndIndex(getEndOfInterval(interval, threadsNumber));
			w.setParent(this);
			thread[interval] = new Thread(w);
		}
		return thread;
	}

	private byte[] initTablicaBajtow() {
		Path path = Paths.get(SCIEZKA_DO_PLIKU);
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
		for (Thread t : threads) {
			t.start();
			if (isDebug()) {
				System.out.println("W�tek " + ++i + " uruchomiony");
			}
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public synchronized int getThreadCounter() {
		return threadCounter;
	}

	private synchronized void setThreadCounter(int threadCounter) {
		this.threadCounter = threadCounter;
	}

	private synchronized int decrementThreadCounterHelper() {
		if (isDebug()) {
			System.out.println("Threads running: "+getThreadCounter());
		}
		return --threadCounter;
	}
	
	public synchronized void decrementThreadCounter() {
		if (decrementThreadCounterHelper()==0) {
			if (isDebug()) {
				System.out.println(Arrays.toString(getHistogram()));
				System.out.println("Liczba bajt�w: " + IntStream.of(getHistogram()).sum());
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Czas wykonania dla "+liczbaW�tk�wNaPocz�tku +
					" w�tk�w:\t " + (endTime - startTime) + "ms");
		}
	}

	public int[] getHistogram() {
		return histogram;
	}

	/**
	 * G��wna metoda s�u��ca do podbicia podanego licznika w histogramie.
	 * Przyjmuje numer indexu histogramu, kt�ry b�dzie podbity.
	 * @param bajt
	 * @return zwraca dotychczasow� liczb� wyst�pie� podanego bajtu.
	 */
	public synchronized int increment(int bajt) {
		return ++this.histogram[bajt];
	}
	
	/**
	 * Metoda helper u�atwiaj�ca liczenie. Ju� nie trzeba sprawdza� warto�ci
	 * w tablicy bajt�w i przekazywa� tej warto�ci do metody increment(int...)
	 * Wystarczy, �e podasz numer indeksu, a reszta zrobi si� za Ciebie.
	 * @param bajtNumber
	 * @return
	 */
	public int incrementHistogramAtByteIndex(int indexWTablicyBajtow) {
		byte index = tablicaBajtow[indexWTablicyBajtow];
		return increment(index);
	}

	/**
	 * Metoda u�atwiaj�ca wykorzystanie increment(int ...) je�li
	 * nie mamy do dyspozycji unsigned int, lecz signed byte.
	 * Jak wiadomo indexy tablicy nie s� podawane w postaci bajt�w, 
	 * tylko w postaci dodatnich liczb ca�kowitych.
	 * Dodatkowo sprawdzenie czy przekszta�cenie nie spowodowa�o
	 * b��du. Rzuca wyj�tkiem w takim wypadku.
	 * @param bajt
	 * @return
	 */
	public int increment(byte bajt) {
		int index = Byte.toUnsignedInt(bajt);
		return increment(index);
	}
	
	public static boolean isDebug() {
		return DEBUG;
	}

	public byte[] getTablicaBajtow() {
		return tablicaBajtow;
	}

	public void setTablicaBajtow(byte[] tablicaBajtow) {
		this.tablicaBajtow = tablicaBajtow;
	}

}