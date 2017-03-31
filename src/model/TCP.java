package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class TCP implements Runnable {

	private Thread thread;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private final Distributor distributor;

	public TCP(Distributor distributor) {
		this.distributor = distributor;
	}

	public boolean connect(String host, int port) {
		String alertMessage;
		try {
			socket = new Socket(host, port);
			System.out.println("Connection established to " + host + " on port " + port);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			return true;
		} catch (UnknownHostException e) {
			alertMessage = "Unknown host address " + host;
		} catch (ConnectException e) {
			System.err.println(e.getMessage());
			alertMessage = "Unable to connect to " + host;
		} catch (IOException e) {
			e.printStackTrace();
			alertMessage = "An Error occurred when connecting to " + host;
		}
		Alert alert = new Alert(AlertType.ERROR, alertMessage, ButtonType.OK);
		alert.showAndWait();
		return false;
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, "TCP-Input");
			thread.start();
			System.out.println("Starting " + thread.getName() + " Thread");
		}
	}

	@Override
	public void run() {
		while (thread != null) {
			try {
				byte packageType = input.readByte();
				Object information = input.readObject();
				distributor.addToQueue(packageType, information);
			} catch (SocketException e) {
				if (socket.isClosed()) {
					System.err.println(e.getMessage());
				} else {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				stop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void stop() {
		if (thread != null) {
			System.out.println("Stopping " + thread.getName() + " Thread");
			thread = null;
		}
	}

	public void disconnect() {
		try {
			if (socket != null) {
				socket.close();
			}
			stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return (socket != null && socket.isConnected());
	}

	public synchronized void sendCommand(String command) {
		try {
			if (socket != null && !socket.isClosed()) {
				output.writeUTF(command);
				output.flush();
				System.out.println(command);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}