package model;

import java.util.EventObject;
import java.util.List;

public class SongListUpdateEvent extends EventObject {

	private static final long serialVersionUID = 9119945588840159377L;
	private List<Song> songList;

	public SongListUpdateEvent(Object source) {
		super(source);
	}

	public SongListUpdateEvent(Object source, List<Song> songList) {
		super(source);
		this.songList = songList;
	}

	public List<Song> getSongList() {
		return songList;
	}

}
