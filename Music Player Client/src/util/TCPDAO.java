package util;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.Album;
import model.Artist;
import model.Song;

public class TCPDAO {
	private ArrayList<Artist> artistsDataObjArr;
	private ArrayList<Song> songs;

	public void connect(String ip) throws Exception {
		try {
			Socket clientSocket = new Socket(ip, 6789);
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			outToServer.writeObject("send");

			artistsDataObjArr = (ArrayList<Artist>) inFromServer.readObject();
			songs = new ArrayList<Song>();

			System.out.println("Got Object");
			for (Artist item : artistsDataObjArr) {
				for (Album album : item.getAlbumList()) {
					for (Song song : album.getSongList()) {
						song.setArtist(item.getName());
						song.setAlbum(album.getName());
						songs.add(song);
					}
				}
			}

			clientSocket.close();
		} catch (ConnectException ex) {
			Alert alert = new Alert(AlertType.ERROR, "Unable to connect to server!", ButtonType.OK);
			alert.showAndWait();
		} // End Try
	}

	public ArrayList<Artist> getArtists() {
		return artistsDataObjArr;
	}

	public ArrayList<Song> getSongs() {
		return songs;
	}
}
