package musicLibrary;
import java.io.Serializable;
import java.util.Comparator;

public class Song implements Serializable, Comparable<Song>, Comparator<Song>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5576066606363296036L;
	
	private String path;
	private String name;
	private String album;
	private String artist;
	private int	trackNumber;
	
	public Song(String path, String name, int trackNumber) {
		this.name = name;
		this.path = path;
		this.trackNumber = trackNumber;
	}
	
	
	
	public String getAlbum() {
		return album;
	}



	public void setAlbum(String album) {
		this.album = album;
	}



	public String getArtist() {
		return artist;
	}



	public void setArtist(String artist) {
		this.artist = artist;
	}



	public String getPath() {
		return path;
	}
	public void setF(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public int getTrackNumber() {
		return trackNumber;
	}


	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}


	@Override
	public int compareTo(Song s) {
		return trackNumber - s.getTrackNumber();
	}


	@Override
	public int compare(Song s1, Song s2) {
		return s1.getTrackNumber() - s2.getTrackNumber();
	}
	
	
}
