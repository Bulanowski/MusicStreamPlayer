package model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by phil on 7/11/17.
 */
public class QueueDAO implements Runnable {

    private Thread thread;
    private final Distributor distributor;
    private AudioDAO audioDAO;
    private ObservableList<Pair<Integer,Song>> songList;

    public QueueDAO(Distributor distributor) {
        this.distributor = distributor;
        songList = FXCollections.observableArrayList();
        this.start();
    }

    public void setAudioDAO(AudioDAO audioDAO) {
        this.audioDAO = audioDAO;
    }

    public void removeFirst() {
        if(songList.size() > 0) {
            Platform.runLater(() -> songList.remove(0));
        }
    }

    public void addListener(ListChangeListener listener) {
        songList.addListener(listener);
    }

    public ObservableList<Pair<Integer, Song>> getSongList() {
        return songList;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "QueueDAO-Thread");
            thread.start();
            System.out.println("Starting " + thread.getName());
        }
    }


    @Override
    public void run() {
        try {
            while (!thread.isInterrupted()) {
                Object objectReceived = distributor.getFromQueue(PackageType.SONG_QUEUE);
                System.out.println(objectReceived.getClass());
                if(objectReceived instanceof ArrayList) {
                    songList.addAll((ArrayList)objectReceived);
                }
                else if(objectReceived instanceof Pair) {
                    boolean bool = (boolean)((Pair) objectReceived).getKey();
                    Pair<Integer,Song> songPair = (Pair)((Pair) objectReceived).getValue();
                 if(bool) {
                     if(audioDAO.getPlaying()) {
                         Platform.runLater(() ->  songList.add(songPair));
                     }

                 } else {
                     for (Pair pair:songList) {
                         if(pair.getKey() == songPair.getKey()) {
                             Platform.runLater(() ->songList.remove(songPair));
                         }
                     }
                 }
                } else {
                    System.err.println("Queue received "+objectReceived.getClass()+" instead of pair or array-list.");
                }

            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        if (thread != null) {
            System.out.println("Stopping " + thread.getName());
            thread.interrupt();
        }
    }
}
