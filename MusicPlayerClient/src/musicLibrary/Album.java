package musicLibrary;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Album implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8311682721792503138L;
	
	ArrayList<Song> songs;
	String name;
	
	public Album(String name) {
		songs = new ArrayList<>();
		this.name = name;
	}
	
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public void addSong(Song s) {
		int pos = Collections.binarySearch(songs, s);
	    if (pos < 0) {
	        add(-pos-1, s);
	    }
	}
	
	public void add(int index, Song s) {
		songs.add(index, s);
	}
	
	public ArrayList<Song> getSongList() {
		return songs;
	}
	
	public Song getSong(String name) {
		for (Song song : songs) {
			if(song.getName().equals(name)){
				return song;
			}
		}
		return null;
	}
}
