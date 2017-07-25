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
                    songList.clear();
                    songList.addAll((ArrayList)objectReceived);
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
