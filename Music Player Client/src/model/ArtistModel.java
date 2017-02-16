package model;

import java.util.ArrayList;

public class ArtistModel {
	ArrayList<Artist> artists;
	ArrayList<Album> albums;

	public ArtistModel(ArrayList<Artist> artists) {
		this.artists = artists;
	}

	public ArrayList<Artist> getArtists() {
		return artists;
	}
}
