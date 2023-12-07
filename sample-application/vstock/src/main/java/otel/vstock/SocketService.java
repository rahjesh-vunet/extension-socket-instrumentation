package otel.vstock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

	@Value("${backend.socket.host}")
	private String backendHost;

	@Value("${backend.socket.port}")
	private int backendPort;

	public String sendOrderAndGetAck(Order order) {

		Socket socket = null;

		try {
			socket = new Socket(backendHost, backendPort);

			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

			// Send the order object to the backend
			outputStream.writeObject(order);

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Receive acknowledgment from the backend
			return reader.readLine();

		} catch (IOException e) {
			e.printStackTrace();
			return "Error communicating with the backend";
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
