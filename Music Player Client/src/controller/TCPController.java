package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.Song;
import model.SongModel;

public class TCPController {
	
	String ip;
	
	public boolean attemptConnection(String address) {
			try {
				Socket clientSocket = new Socket(address, 6789);
				clientSocket.close();
				ip = address;
				return true;
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR, "Unable to connect to server!", ButtonType.OK);
				alert.showAndWait();
			}
		return false;
	}

	public SongModel requestSongs() {
		if (ip == null) {
			Alert alert = new Alert(AlertType.ERROR, "A connections to a server must first be established!", ButtonType.OK);
			alert.showAndWait();
			throw new NullPointerException();
		}
		try {
			Socket clientSocket = new Socket(ip, 6789);
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

			outToServer.writeObject("request_songs");

			@SuppressWarnings("unchecked")
			ArrayList<Song> songs = (ArrayList<Song>) inFromServer.readObject();

			SongModel songModel = new SongModel(songs);

			System.out.println("Got Object");

			clientSocket.close();
			return songModel;
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR, "Unable to connect to server!", ButtonType.OK);
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		throw new NullPointerException();
	}
	
	public void sendCommand(String command) {
		if (ip == null) {
			Alert alert = new Alert(AlertType.ERROR, "A connections to a server must first be established!", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		try {
			Socket clientSocket = new Socket(ip, 6789);
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			outToServer.writeObject(command);
			clientSocket.close();
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR, "Unable to connect to server!", ButtonType.OK);
			alert.showAndWait();
		}
	}

}
