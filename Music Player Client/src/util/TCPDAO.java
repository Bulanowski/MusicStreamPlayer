package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.Song;
import model.SongModel;

public class TCPDAO {
	private SongModel songModel;

	public void connect(String ip) {
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
	}

	public SongModel getSongModel() {
		return songModel;
	}
}
