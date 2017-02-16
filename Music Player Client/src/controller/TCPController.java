package controller;

import model.SongModel;
import util.TCPDAO;

public class TCPController {
	private TCPDAO connection;

	public TCPController() {
		connection = new TCPDAO();
	}

	public SongModel requestSongs(String text) {
		try {
			connection.connect(text);
			SongModel songModel = connection.getSongModel();
			return songModel;
		} catch (Exception e) {
			System.out.println("Unable to Connect");
			e.printStackTrace();
		}
		throw new NullPointerException();
	}

}
