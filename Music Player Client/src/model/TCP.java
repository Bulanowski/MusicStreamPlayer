package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
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

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setName("TCP-Input");
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
				PackageReceivedEvent ev = new PackageReceivedEvent(this, packageType, information);
				for (PackageReceivedListener listener : packageReceivedListeners) {
					listener.readPackage(ev);
				}
			} catch (SocketException e) {
				if (socket.isClosed()) {
					System.err.println(e.getMessage());
				} else {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void stop() {
		System.out.println("Stopping " + thread.getName() + " Thread");
		thread = null;
	}

	public void disconnect() {
		try {
			packageReceivedListeners.clear();
			if (socket != null) {
				socket.close();
			}
			stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return (socket != null ? socket.isConnected() : false);
	}

	public synchronized void sendCommand(String command) {
		try {
			System.out.println(command);
			output.writeUTF(command);
			output.flush();
		} catch (SocketException e) {
			if (socket.isClosed()) {
				System.err.println(e.getMessage());
			} else {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

}
