package model;

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
    private ObservableList<Pair<Integer,Song>> songList;

    public QueueDAO(Distributor distributor) {
        this.distributor = distributor;
        songList = FXCollections.observableArrayList();
        this.start();
    }

    public void addListener(ListChangeListener listener) {
        songList.addListener(listener);
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
                if(objectReceived instanceof ArrayList) {
                    System.out.println("Got Song Queue from Server!");
//                    LinkedHashMap<Integer,Song> songs = (LinkedHashMap<Integer, Song>) objectReceived;
                    songList.clear();
                    songList.addAll((ArrayList)objectReceived);
                    System.out.println("Queue size: "+songList.size());
//                    Iterator iterator = songs.entrySet().iterator();
//                    System.out.println("Clearing song list...");
//                    songList.clear();
//                    while (iterator.hasNext()) {
//                        Map.Entry entry = (Map.Entry) iterator.next();
//
//                        songList.add(new Pair<>((Integer)entry.getKey(),(Song)entry.getValue()));
//                    }
//                    System.out.println("New song list size: "+songList.size());
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (thread != null) {
            System.out.println("Stopping " + thread.getName());
            thread.interrupt();
        }
    }
}
