package model;

import java.io.Serializable;
import java.util.Comparator;

public class Song implements Serializable, Comparable<Song>, Comparator<Song> {

    private static final long serialVersionUID = 4898115281270251697L;

    private final String path;
    private String name;
    private String album;
    private String artist;
    private int trackNumber;
    private int id;
    private int trackLength;

    public Song(String path, int trackNumber) {
        this.path = path;
        this.trackNumber = trackNumber;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getTrackLength() {return trackLength; }

    public String getTrackLengthFormatted() { return trackLength/60+":"+((trackLength%60 < 10)?"0"+trackLength%60:trackLength%60);}

    public void setTrackLength(int trackLength) {this.trackLength = trackLength;}


    @Override
    public int compareTo(Song s) {
        return trackNumber - s.getTrackNumber();
    }

    @Override
    public int compare(Song s1, Song s2) {
        return s1.getTrackNumber() - s2.getTrackNumber();
    }

}
