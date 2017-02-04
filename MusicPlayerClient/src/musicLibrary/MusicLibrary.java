package musicLibrary;
import java.io.Serializable;
import java.util.ArrayList;

public class MusicLibrary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7167930443574601313L;
	ArrayList<Artist> artists;
	
	public MusicLibrary() {
		artists = new ArrayList<>();
	}
	
	public void addArtist(Artist a) {
		artists.add(a);
	}
	
	public ArrayList<Artist> getArtistList() {
		return artists;
	}
	
	public ArrayList<Artist> findArtist(String s) {
		s = s.toLowerCase();
		ArrayList<Artist> a = new ArrayList<>();
		for (Artist artist : artists) {
			if(artist.getName().toLowerCase().contains(s)) {
				a.add(artist);
			}
		}
		return a;
	}
	
	public Artist getArtist(String name) {
		for (Artist artist : artists) {
			if(artist.getName().equals(name)) {
				return artist;
			}
		}
		return null;
	}
	
}
