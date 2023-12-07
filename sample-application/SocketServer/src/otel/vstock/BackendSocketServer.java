package otel.vstock;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackendSocketServer {

	private static String DATE_FORMAT = "dd/MM/yy HH:mm:ss.SSS";

	public static void main(String[] args) {

		int port = 18080;
		if (args.length < 1) {
			System.out.println(new SimpleDateFormat(DATE_FORMAT).format(new Date())
					+ " Starting server with default port (18080)");
		} else {
			port = Integer.parseInt(args[0]);
			System.out.println(new SimpleDateFormat(DATE_FORMAT).format(new Date()) + " Starting server with the port ("
					+ port + ")");
		}
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println(new SimpleDateFormat(DATE_FORMAT).format(new Date())
					+ " Backend socket server is running at port " + port + "...");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println(new SimpleDateFormat(DATE_FORMAT).format(new Date()) + " Client connected: "
						+ socket.getInetAddress());

				// Receive order from the Spring Boot API
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Order receivedOrder = (Order) inputStream.readObject();
				System.out.println(
						new SimpleDateFormat(DATE_FORMAT).format(new Date()) + " Received order: " + receivedOrder);

				// Process the order (add your business logic here)

				// Send acknowledgment back to the Spring Boot API
				sendAck(socket);

				System.out.println(new SimpleDateFormat(DATE_FORMAT).format(new Date()) + " Sent ack.");
				socket.close();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void sendAck(Socket socket) throws IOException {
		// Assuming a simple acknowledgment message
		String ackMessage = "Order received successfully!";
		socket.getOutputStream().write(ackMessage.getBytes());
		System.out.println(new SimpleDateFormat(DATE_FORMAT).format(new Date()) + " Sending ack.");
	}
}