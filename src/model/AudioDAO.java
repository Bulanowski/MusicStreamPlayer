package model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.util.Pair;

public class AudioDAO implements Runnable {

    private Thread thread;
    private Thread timer;
    private final Distributor distributor;
    private Pair<SimpleStringProperty, SimpleStringProperty> songInfo;
    private Pair<SimpleIntegerProperty, SimpleIntegerProperty> trackLengthAndPosition;
    private ByteBuffer byteBuffer;
    private byte[] audioBuffer;
    private int size = 0;
    private int bytesRead = 0;
    private volatile boolean playing = false;
    private static final double BUFFER_PERCENTAGE = 0.05; // default = 0.05
    private final QueueDAO queueDAO;
    private AudioPlayer player;
    private boolean skipped = false;

    public AudioDAO(Distributor distributor,QueueDAO queueDAO) {
        this.queueDAO = queueDAO;
        this.distributor = distributor;
        songInfo = new Pair(new SimpleStringProperty(),new SimpleStringProperty());
        trackLengthAndPosition = new Pair<>(new SimpleIntegerProperty(), new SimpleIntegerProperty());



    }

    public Pair getSongInfo() {
        return songInfo;
    }

    public Pair getTrackLengthAndPosition() {return trackLengthAndPosition; }

    public void setAudioPlayer(AudioPlayer player) {
        this.player = player;
    }


    public void start() {
        if (thread == null) {
            thread = new Thread(this, "AudioDAO-Thread");
            thread.start();
            System.out.println("Starting " + thread.getName());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            while (!thread.isInterrupted()) {
                Object objectReceived = distributor.getFromQueue(PackageType.SONG);
                if (objectReceived instanceof Pair) {
                    skipped = false;
                    Pair<Integer, Song> sizeAndSong = (Pair<Integer, Song>) objectReceived;
                    trackLengthAndPosition.getValue().set(sizeAndSong.getValue().getTrackLength());
                    trackLengthAndPosition.getKey().set(0);
                    songInfo.getKey().set(sizeAndSong.getValue().getArtist());
                    songInfo.getValue().set(sizeAndSong.getValue().getName());
                    resetVars(sizeAndSong.getKey());
                } else if (objectReceived instanceof byte[]) {
                    byte[] buffer = (byte[]) objectReceived;
                    addToAudioBuffer(buffer);
                    if (!playing && bytesRead >= (size * BUFFER_PERCENTAGE)) {
                        startPlaying();
                    }
                } else if (objectReceived instanceof String) {
                    if(objectReceived.equals("stop")) {
                        player.stopPlayer();
                        skipped = true;
                    }
                }
            }
        } catch (InterruptedException e) {
			System.err.println(thread.getName() + " was interrupted");
        } finally {
            thread = null;
        }
    }

    public void startTimer() {
            System.out.println("Starting timer ");
            Runnable r = () -> {
                while (trackLengthAndPosition.getKey().getValue() < trackLengthAndPosition.getValue().getValue() && !timer.isInterrupted()) {
                    try {
                        trackLengthAndPosition.getKey().set(trackLengthAndPosition.getKey().getValue() + 1);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("ending timer");
                trackLengthAndPosition.getKey().set(-1);
            };
        if(timer == null) {
            timer = new Thread(r, "Timer-Thread");
            timer.start();
        } else if(!timer.isAlive()) {
            timer = new Thread(r, "Timer-Thread");
            timer.start();
        }
    }

    public void stop() {
        if (thread != null) {
            System.out.println("Stopping " + thread.getName());
            thread.interrupt();
        }

        if(timer != null) {
            System.out.println("Stopping " + timer.getName());
            timer.interrupt();
        }
    }

    private void addToAudioBuffer(byte[] buffer) {
        bytesRead += buffer.length;
        byteBuffer.put(buffer);
    }

    private void resetVars(int size) {
        bytesRead = 0;
        this.size = size;
        audioBuffer = new byte[size];
        byteBuffer = ByteBuffer.wrap(audioBuffer);
    }

    public byte[] getAudioBuffer() {
        return audioBuffer;
    }

    public boolean getPlaying() {
        return playing;
    }

    private synchronized void startPlaying() {
        System.out.println("Start playing");
        playing = true;
        notifyAll();
        songInfo.getValue().set(songInfo.getValue().getValue()+" ");
    }

    public void stopPlaying() {
        queueDAO.removeFirst();
        songInfo.getKey().set("");
        songInfo.getValue().set("");
        System.out.println("Stop playing");
        playing = false;
        try {
            if(!skipped) {
                distributor.addToQueue(PackageType.COMMAND, "songEnd");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
