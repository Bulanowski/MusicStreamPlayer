package model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel implements Observer {

    private final ObservableList<Song> songList;

    public SongModel(SongDAO songDAO) {
        songList = FXCollections.observableArrayList();
        songDAO.addObserver(this);
        songDAO.start();
    }

    public void clear() {
        songList.clear();
    }

    public ObservableList<Song> getSongs() {
        return songList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object obj) {
        songList.addAll((List<Song>) obj);
    }

}
