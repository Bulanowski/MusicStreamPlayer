package model;

import java.util.List;

public interface SongDAO {
	
	public Song get(String songPath);
	
	public List<Song> getAll();
	
	public boolean update(String songPath, Song song);
	
	public boolean delete(String songPath);
	
	public boolean create(Song song);

}
