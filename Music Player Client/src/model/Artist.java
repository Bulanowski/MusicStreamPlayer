package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Artist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7876277859060079950L;

	ArrayList<Album> albums;
	String name;

	public Artist(String name) {
		albums = new ArrayList<>();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addAlbum(Album a) {
		albums.add(a);
	}

	public ArrayList<Album> getAlbumList() {
		return albums;
	}

	public Album getAlbum(String s) {
		for (Album album : albums) {
			if (album.getName().equals(s)) {
				return album;
			}
		}
		return null;
	}
}
