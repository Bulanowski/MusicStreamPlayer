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
	
	private SongModel songModel;
	
	public SongModel requestSongs(String ip) {
		try {
			Socket clientSocket = new Socket(ip, 6789);
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			outToServer.writeObject("send");

			ArrayList<Song> songs = (ArrayList<Song>) inFromServer.readObject();
			
			songModel = new SongModel(songs);

			System.out.println("Got Object");

			clientSocket.close();
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR, "Unable to connect to server!", ButtonType.OK);
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return songModel;
	}

//	private TCPDAO connection;
//
//	public TCPController() {
//		connection = new TCPDAO();
//	}
//
//	public SongModel requestSongs(String text) {
//		try {
//			connection.connect(text);
//			SongModel songModel = connection.getSongModel();
//			return songModel;
//		} catch (IOException e) {
//			System.out.println("Unable to Connect");
//			e.printStackTrace();
//		}
//		throw new ConnectException();
//	}

}
