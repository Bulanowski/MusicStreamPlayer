package model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel {
	private ObservableList<Song> songs;

	public SongModel(ArrayList<Song> songData) {
		songs = FXCollections.observableArrayList();
		songs.addAll(songData);
	}

	public ObservableList<Song> getSongs() {
		return songs;
	}

	public int size() {
		return songs.size();
	}

}
