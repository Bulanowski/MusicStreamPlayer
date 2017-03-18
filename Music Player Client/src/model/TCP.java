package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class TCP implements Runnable {

	private Thread thread;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private final LinkedList<PackageReceivedListener> packageReceivedListeners;
	
	public TCP() {
		packageReceivedListeners = new LinkedList<PackageReceivedListener>();
	}

	public void connect(String ipAddress, int port) {
		try {
			socket = new Socket(ipAddress, port);
			System.out.println("Connection established to " + ipAddress + " on port " + port);
			open();
		} catch (IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Unable to connect to server!", ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	public void open() throws IOException {
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}
	
	public void addPackageReceivedListener(PackageReceivedListener packageReceivedListener) {
		packageReceivedListeners.add(packageReceivedListener);
	}
	
//	public void close() {
//		try {
//			if (socket != null) {
//				socket.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		while (thread != null) {
			try {
				byte packageType = input.readByte();
				Object information = input.readObject();
				PackageReceivedEvent ev = new PackageReceivedEvent(this, packageType, information);
				for (PackageReceivedListener listener : packageReceivedListeners) {
					listener.readPackage(ev);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void stop() {
		thread = null;
	}
	
	public void disconnect() {
		try {
			sendCommandWithoutReturn("end_connection");
			if (socket != null) {
				socket.close();
			}
			stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return socket.isConnected();
	}
	
	public synchronized void sendCommandWithoutReturn(String command) {
		try {
			if (socket != null) {
				System.out.println(command);
				output.writeUTF(command);
				output.flush();
			} else {
				Alert alert = new Alert(AlertType.ERROR, "A connection has not yet been established!", ButtonType.OK);
				alert.showAndWait();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Failed to send command!", ButtonType.OK);
			alert.showAndWait();
		}
	}

	public synchronized Object sendCommandWithReturn(String command) {
		try {
			if (socket != null) {
				output.writeUTF(command);
				System.out.println(command);
				output.flush();
				if (input.readByte() == PackageType.SONG_LIST.getByte()) {
					return input.readObject();
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR, "A connection has not yet been established!", ButtonType.OK);
				alert.showAndWait();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Failed to send command!", ButtonType.OK);
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

}
