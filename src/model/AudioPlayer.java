package model;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class AudioPlayer implements Runnable {

	private Thread thread;
	private final AudioDAO audioDAO;
	private final ChangeListener<Number> volumeChangeListener;
	private final ChangeListener<Boolean> skipListener;
    private CustomAudioDevice customAudioDevice;
	private Player player;
	float oldVolume;

	public AudioPlayer(AudioDAO audioDAO) {
		this.audioDAO = audioDAO;
		volumeChangeListener = (observable, oldValue, newValue) -> {
            if (customAudioDevice != null) {
                customAudioDevice.setVolume(oldValue.floatValue());
            } else {
            	oldVolume = oldValue.floatValue();
			}
        };

        skipListener = (observableValue,oldVal,newVal) -> {
            if(newVal) {

            }
        };

		audioDAO.start();
	}

	public Method getStopPlayerMethod() {
        try {
            return this.getClass().getMethod("stopPlayer");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setName("Audio-Player");
			thread.start();
			System.out.println("Starting " + thread.getName() + " Thread");
		}
	}

	@Override
	public void run() {
		try {
			while (thread != null) {
				synchronized (audioDAO) {
					while (!audioDAO.getPlaying()) {
						audioDAO.wait();
					}
				}
				System.out.println("Playing");
//                float oldVolume = (customAudioDevice != null ? customAudioDevice.getVolume() : 0.0f);
                customAudioDevice = new CustomAudioDevice();
                customAudioDevice.setVolume(oldVolume);
                try {
                    player = new Player(new ByteArrayInputStream(audioDAO.getAudioBuffer()), customAudioDevice);
                    audioDAO.startTimer();
					player.play();
					player = null;
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
				audioDAO.stopPlaying();
			}
		} catch (InterruptedException e) {
//			System.err.println(thread.getName() + " was interrupted");
		}
	}

	public void stopPlayer() {
        if (player != null) {
            player.close();
        }
        player = null;
    }


	public void stop() {
        if (player != null) {
            player.close();
        }
		if (thread != null) {
			System.out.println("Stopping " + thread.getName() + " Thread");
			thread.interrupt();
			thread = null;
		}
	}

	public ChangeListener<Number> getVolumeChangeListener() {
		return volumeChangeListener;
	}

    public ChangeListener<Boolean> getSkipListener() { return skipListener; }
}
