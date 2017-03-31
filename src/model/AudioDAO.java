package model;

import java.nio.ByteBuffer;

import javafx.util.Pair;

public class AudioDAO implements Runnable {

    private Thread thread;
    private final Distributor distributor;
    private ByteBuffer byteBuffer;
    private byte[] audioBuffer;
    private int size = 0;
    private int bytesRead = 0;
    private volatile boolean playing = false;
    private volatile boolean gotSongInfo = false;
    private static final double BUFFER_PERCENTAGE = 0.05; // default = 0.05

    public AudioDAO(Distributor distributor) {
        this.distributor = distributor;
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
                Object objectReceived;
                if (!gotSongInfo) {
                    objectReceived = distributor.getFromQueue(PackageType.SONG_INFO);
                    if (objectReceived instanceof Pair) {
                        Pair<Integer, Song> sizeAndSong = (Pair<Integer, Song>) objectReceived;
                        resetVars(sizeAndSong.getKey());
                        gotSongInfo = true;
                    }
                } else {
                    objectReceived = distributor.getFromQueue(PackageType.AUDIO);
                    if (objectReceived instanceof byte[]) {
                        byte[] buffer = (byte[]) objectReceived;
                        addToAudioBuffer(buffer);
                        if (!playing && bytesRead >= (size * BUFFER_PERCENTAGE)) {
                            startPlaying();
                        }
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

    private void startPlaying() {
        System.out.println("Start playing");
        playing = true;
        notifyAll();
    }

    public void stopPlaying() {
        System.out.println("Stop playing");
        playing = false;
        gotSongInfo = false;
        try {
            distributor.addToQueue(PackageType.COMMAND.getByte(), "songEnd");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
