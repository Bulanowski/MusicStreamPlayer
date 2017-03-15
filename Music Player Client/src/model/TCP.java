package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class TCP {

	private Socket socket;
	private ObjectOutputStream oos;

	public void connect(String ipAddress, int port) {
		try {
			socket = new Socket(ipAddress, port);
			System.out.println("Connection established to " + ipAddress + " on port " + port);
			oos = new ObjectOutputStream(getOutputStream());
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR, "Unable to connect to server!", ButtonType.OK);
			alert.showAndWait();
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return socket.isConnected();
	}

	public void sendCommand(String command) {
		try {
			if (socket != null) {
				System.out.println(command);
				oos.writeUTF(command);
				oos.flush();
			} else {
				Alert alert = new Alert(AlertType.ERROR, "A connection has not yet been established!", ButtonType.OK);
				alert.showAndWait();
			}
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR, "Failed to send command!", ButtonType.OK);
			alert.showAndWait();
		}
	}

	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

}
