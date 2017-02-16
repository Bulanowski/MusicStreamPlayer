package controller;

import model.ArtistModel;
import model.SongModel;
import util.TCPDAO;

public class TCPController {
	private TCPDAO connection;
	private ArtistModel artistModel;
	private SongModel songModel;

	public TCPController() {
		connection = new TCPDAO();
	}

	public void connect(String text) {
		try {
			connection.connect(text);
			artistModel = new ArtistModel(connection.getArtists());
			songModel = new SongModel(connection.getSongs());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to Connect");
			e.printStackTrace();
		}
	}

	public ArtistModel getArtistModel() {
		return artistModel;
	}

	public SongModel getSongModel() {
		return songModel;
	}

}
