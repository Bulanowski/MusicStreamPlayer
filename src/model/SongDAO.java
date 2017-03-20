package model;

import model.event_handling.SongListUpdateListener;

public interface SongDAO {
	
	public void setSongListUpdateListener(SongListUpdateListener songListUpdateListener);
	
	public Song get(String songPath);
	
	public void requestSongs();
	
	public boolean update(String songPath, Song song);
	
	public boolean delete(String songPath);
	
	public boolean create(Song song);

}
