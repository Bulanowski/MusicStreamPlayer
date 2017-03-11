package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel {
	private ObservableList<Song> songs;
	SongDAO source;

	public SongModel(SongDAO songDAO) {
		source = songDAO;
		songs = FXCollections.observableArrayList();
		songs.addAll(source.getAll());
	}

	public ObservableList<Song> getSongs() {
		return songs;
	}

}
