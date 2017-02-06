package tcp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import gui.MainStage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import musicLibrary.Album;
import musicLibrary.Artist;
import musicLibrary.Song;

public class TCPClient {
	
	public static void connect(String ip) throws Exception {
		try {
			Socket clientSocket = new Socket(ip, 6789);
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
	//		outToServer.writeObject("send");
			@SuppressWarnings("unchecked")
			ArrayList<Artist> ar = (ArrayList<Artist>) inFromServer.readObject();
			System.out.println("Got Object");
			for (Artist artist : ar) {
				TreeItem<String> artistItem = new TreeItem<String>(artist.getName());
				ArrayList<Album> albums = artist.getAlbumList();
				for (Album album : albums) {
					TreeItem<String> albumItem = new TreeItem<String>(album.getName());
					ArrayList<Song> songs = album.getSongList();
					for (Song song : songs) {
						song.setArtist(artist.getName());
						song.setAlbum(album.getName());
						MainStage.addToTable(song);
					}
					artistItem.getChildren().add(albumItem);
				}
				MainStage.addToTree(artistItem);
			}
			
			clientSocket.close();
		} catch (ConnectException ex) {
			Alert alert = new Alert(AlertType.ERROR, "Unable to connect to server!", ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	
}
