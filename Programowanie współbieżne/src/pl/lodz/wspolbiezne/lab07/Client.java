package pl.lodz.wspolbiezne.lab07;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Logger;

public class Client {

	private final int LICZBA_PROCESOR�W = 1;
	private final int N = 64;
	private Logger logger;
	private long start;

	// NIESTETY JAVA NIE JEST TAKA SPRYTNA I MUSZ� POWT�RZY� TO TRZY RAZY,
	// ABY KOMPILATOR PRZYPISA� INNE ADRESY
	double[][] A = new double[N][N];
	double[][] B = new double[N][N];
	double[][] C = new double[N][N];

	public Client(String hostName, int portNumber) {
		start = System.currentTimeMillis();
		logger = Obliczenia.getCustomLogger();
		logger.info("Connecting to server at port: " + portNumber + " ...");
		for (int i = 0; i < N; i++) {
			A[i] = new Random().doubles(N).toArray();
			B[i] = new Random().doubles(N).toArray();
			C[i] = new Random().doubles(N).toArray();
		}
		try {
			Socket kkSocket = new Socket(hostName, portNumber);
			int receiveBufferSize = kkSocket.getReceiveBufferSize();
			logger.info("Rozmiar bufora: ("
					+ receiveBufferSize
					+ " bytes) ("
					+ Obliczenia.humanReadableByteCount(receiveBufferSize,
							false) + ")");
			dispatch(kkSocket);
			logger.info("Zako�czono obliczanie.");
			System.out.println("Ca�kowity czas wykonania: "
					+ (int) ((System.currentTimeMillis() - start) / 1000)
					+ " sekund.");
		} catch (UnknownHostException e) {
			logger.severe("Don't know about host " + hostName);
			System.exit(1);
		} catch (StreamCorruptedException e) {
			logger.severe("This constructor will block until the corresponding"
					+ " ObjectOutputStream has written and flushed the header.");
			logger.severe(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			logger.severe("Couldn't get I/O for the connection to " + hostName);
			logger.severe(e.getMessage());
			System.exit(1);
		} catch (ClassNotFoundException e) {
			logger.severe("�le skastowany typ int[][][] / double[][][]");
			System.exit(1);
		}
	}

	@SuppressWarnings("unused")
	private void dispatch(Socket kkSocket) throws IOException,
			ClassNotFoundException {

		logger.info("Trwa mno�enie macierzy AxB");
		Obliczenia obliczenia = new Obliczenia(A, B);
		double[][] AB = multiply(kkSocket, obliczenia);

		logger.info("Trwa mno�enie macierzy ABxC");
		obliczenia = new Obliczenia(AB, C);
		double[][] ABC = multiply(kkSocket, obliczenia);

		if (N <= 8) {
			System.out.println(Obliczenia.toString(ABC));
		}
	}

	private double[][] multiply(Socket kkSocket, Obliczenia obliczenia)
			throws IOException, ClassNotFoundException {

		OutputStream outputStream = kkSocket.getOutputStream();
		BufferedOutputStream bufferedOut = new BufferedOutputStream(
				outputStream);
		ObjectOutputStream oos = new ObjectOutputStream(bufferedOut);
		oos.flush();

		for (int proces = 0; proces < LICZBA_PROCESOR�W; proces++) {
			int start = getBeginningOfInterval(proces, LICZBA_PROCESOR�W);
			int end = getEndOfInterval(proces, LICZBA_PROCESOR�W);
			MacierzeDto C = obliczenia.getBlock(start, end);
			Logger.getGlobal().info("Trwa wysy�ka bloku nr " + proces);
			long startTime = System.currentTimeMillis();

			Object[] c = new Object[1];
			oos.writeObject(c);
			oos.flush();
			int sizeOfC = Obliczenia.sizeOf(C);
			long duration = System.currentTimeMillis() - startTime;
			logger.info("Zako�czono przesy�anie bloku nr " + proces + " ("
					+ Obliczenia.humanReadableByteCount(sizeOfC, false) + ")");
			long speed = (long) (sizeOfC / (duration / 1000000d));
			logger.info("Write speed: "
					+ Obliczenia.humanReadableByteCount(speed, false) + "/s");
		}

		// tutaj dodaj wyniki, kt�re przyjd� z powrotem z serwera.
		// Pami�taj, �eby przy zliczaniu wzi�� pod uwag� indeksy kolumn.

		InputStream inputStream = kkSocket.getInputStream();

		ResultDto macierze;
		double[][] AB = new double[N][N];
		int i = LICZBA_PROCESOR�W;
		int size;
		while (true) {
			if (inputStream.available() != 0) {
				logger.info("Stream available");
				BufferedInputStream bufferedIn = new BufferedInputStream(
						inputStream);
				ObjectInputStream ois = new ObjectInputStream(bufferedIn);

				if ((macierze = (ResultDto) ois.readObject()) != null) {
					size = Obliczenia.sizeOf(macierze);
					logger.info("Trwa odbieranie "
							+ Obliczenia.humanReadableByteCount(size, false));
					// long startTime = System.currentTimeMillis();
					// macierze = (ResultDto) ois.readObject();
					// long duration = System.currentTimeMillis()-startTime;
					// logger.info("Read speed: " +
					// (size/(duration/1000000d))+" kB/s");
					obliczenia.merge(macierze, AB);
					if (--i == 0)
						break;
				}
			}
		}
		return AB;

	}

	public int getBeginningOfInterval(int interval, int totalIntervals) {
		if (totalIntervals <= interval) {
			throw new IllegalArgumentException(
					"Przedzia� nie mo�e by� wi�kszy ni�: " + totalIntervals
							+ " a podano: " + interval);
		}
		double fraction = (double) interval / (double) totalIntervals;
		return (int) (fraction * N);
	}

	public int getEndOfInterval(int interval, int totalIntervals) {
		if (totalIntervals <= interval) {
			throw new IllegalArgumentException(
					"Przedzia� nie mo�e by� wi�kszy ni�: " + totalIntervals
							+ " a podano: " + interval);
		}
		double rozmiarPrzedzialu = (double) N / (double) totalIntervals;
		double fraction = (double) interval / (double) totalIntervals;
		return (int) ((fraction * N) + rozmiarPrzedzialu);
	}

	public static void main(String[] args) {
		new Client("localhost", 4444);
	}
}
