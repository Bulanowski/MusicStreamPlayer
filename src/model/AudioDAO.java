package model;

import java.nio.ByteBuffer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

public class AudioDAO implements Runnable {

    private Thread thread;
    private final Distributor distributor;
    private Pair<SimpleStringProperty, SimpleStringProperty> songInfo;
    private ByteBuffer byteBuffer;
    private byte[] audioBuffer;
    private int size = 0;
    private int bytesRead = 0;
    private volatile boolean playing = false;
    private static final double BUFFER_PERCENTAGE = 0.05; // default = 0.05

    public AudioDAO(Distributor distributor) {
        this.distributor = distributor;
        songInfo = new Pair(new SimpleStringProperty(),new SimpleStringProperty());
    }

    public Pair getSongInfo() {
        return songInfo;
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
                    Pair<Integer, Song> sizeAndSong = (Pair<Integer, Song>) objectReceived;
                    songInfo.getKey().set(sizeAndSong.getValue().getArtist());
                    songInfo.getValue().set(sizeAndSong.getValue().getName());
                    resetVars(sizeAndSong.getKey());
                } else if (objectReceived instanceof byte[]) {
                    byte[] buffer = (byte[]) objectReceived;
                    addToAudioBuffer(buffer);
                    if (!playing && bytesRead >= (size * BUFFER_PERCENTAGE)) {
                        startPlaying();
                    }
                }
            }
        } catch (InterruptedException e) {
//			System.err.println(thread.getName() + " was interrupted");
        } finally {
            thread = null;
        }
    }

    public void stop() {
        if (thread != null) {
            System.out.println("Stopping " + thread.getName());
            thread.interrupt();
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
        System.out.println("Stop playing");
        playing = false;
        try {
            distributor.addToQueue(PackageType.COMMAND.getByte(), "songEnd");
            if(distributor.isEmpty(PackageType.SONG.getByte())) {
                System.out.println("EMPTY");
                songInfo.getKey().set("");
                songInfo.getValue().set("");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
