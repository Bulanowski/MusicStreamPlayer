package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.event_handling.SongListUpdateEvent;
import model.event_handling.SongListUpdateListener;

public class SongModel {

	private final ObservableList<Song> songs;
	private final SongDAO source;

	public SongModel(SongDAO songDAO) {
		songs = FXCollections.observableArrayList();
		source = songDAO;
		source.setSongListUpdateListener(new SongListUpdateListener() {

			@Override
			public void songListUpdate(SongListUpdateEvent ev) {
				songs.addAll(ev.getSongList());
			}

		});
	}
	
	public void requestSongs() {
		source.requestSongs();
	}

	public void clear() {
		songs.clear();
	}

	public ObservableList<Song> getSongs() {
		return songs;
	}

}
