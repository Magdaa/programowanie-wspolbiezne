package pl.lodz.wspolbiezne.lab07;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Cluster {
	Logger logger;

	public Cluster(int portNumber) {
		logger = Obliczenia.getCustomLogger();
		logger.info("Starting server at port: "+portNumber+" ...");
		try {
			ServerSocket serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = serverSocket.accept();

			processInput(clientSocket);
			serverSocket.close();

		} catch (IOException e) {
			System.out
					.println("Exception caught when trying to listen on port "
							+ portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("�le skastowany typ int[][][] / double[][][]");
			System.exit(1);

		}
		logger.info("Server started at 127.0.0.1 port "+portNumber);
	}

	private void processInput(Socket kkSocket) throws IOException,
			ClassNotFoundException {
		InputStream inputStream = kkSocket.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(inputStream);
		OutputStream outputStream = kkSocket.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(outputStream);

		MacierzeDto macierze;
		while ((macierze = (MacierzeDto) ois.readObject()) != null) {
			logger.info("Przyj�to macierz do obliczenia");
//			if (ois.equals("bye")) { // albo kiedy ca�a macierz zosta�a
										// wype�niona
//				ois.close();
//				oos.close();
//				kkSocket.close();
//				break;
//			}
			// Initiate conversation with client
			 Obliczenia obliczenia = new Obliczenia();
			 ResultDto result = obliczenia.processInput(macierze);
			// out.println(outputLine);

			// while ((inputLine = in.readLine()) != null) {
			// outputLine = obliczenia.processInput(inputLine);
			// out.println(obliczenia.tooutputLine);
			// }

//			oos.writeObject("bye"); // daj znak ko�ca
			oos.writeObject(result);
		}
	}
	public static void main(String[] args) {
		new Cluster(4444);
	}
}
