package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class TCP {

	private Socket socket;

	public void connect(String ipAddress, int port) {
		try {
			socket = new Socket(ipAddress, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	public void sendCommand(String command) {
		try {
			if (socket != null) {
				ObjectOutputStream outToServer = new ObjectOutputStream(getOutputStream());
				outToServer.writeObject(command);
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
