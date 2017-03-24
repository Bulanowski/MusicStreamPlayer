package model;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel implements Observer<List<Song>> {

	private final SongDAO source;
	private final ObservableList<Song> songList;

	public SongModel(SongDAO songDAO) {
		songList = FXCollections.observableArrayList();
		source = songDAO;
		source.register(this);
	}

	public void clear() {
		songList.clear();
	}

	public ObservableList<Song> getSongs() {
		return songList;
	}

	@Override
	public void update(List<Song> e) {
		songList.addAll(e);
	}

}
