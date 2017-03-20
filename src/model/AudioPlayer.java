package model;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import model.event_handling.AudioPlayingEvent;
import model.event_handling.AudioPlayingListener;
import model.event_handling.VolumeEvent;
import model.event_handling.VolumeListener;

public class AudioPlayer implements Runnable {

	private Thread thread;
	private Playback playback;
	private final AudioDAO audioDAO;
	private FloatControl volume;
	private AudioPlayingListener audioPlayingListener;
	private VolumeListener volumeListener;
	private volatile boolean bForceStop = false;

	public AudioPlayer(AudioDAO audioDAO) {
		this.audioDAO = audioDAO;
	}

	public void start() {
		if (thread == null) {
			bForceStop = false;
			thread = new Thread(this);
			thread.setName("Audio-Player");
			thread.start();
			System.out.println("Starting " + thread.getName() + " Thread");
		}
	}

	@Override
	public void run() {
		while (thread != null) {
			try {
				if (audioDAO.getPlaying()) {
					Thread.sleep(1000);
					System.out.println("Playing");
					playback = new Playback();
					playback.start();
					audioDAO.stopPlaying();
				}
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.err.println(thread.getName() + " was interrupted");
			}
		}
	}

	public void stop() {
		if (playback != null) {
			System.out.println("Force stopping Playback");
			forceStop();
		}
		if (thread != null) {
			System.out.println("Stopping " + thread.getName() + " Thread");
			thread = null;
		}
	}

	public void setAudioPlayingListener(AudioPlayingListener audioPlayingListener) {
		this.audioPlayingListener = audioPlayingListener;
	}

	public void forceStop() {
		bForceStop = true;
	}

	class Playback {

		private void start() {
			try {
				AudioInputStream audioInStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioDAO.getAudioBuffer()));
				AudioFormat format = audioInStream.getFormat();
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(),
						16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
				audioInStream = AudioSystem.getAudioInputStream(format, audioInStream);
				// getLine
				SourceDataLine line = AudioSystem.getSourceDataLine(format);
				line.open(format);
				System.out.println("Opened line with buffer size " + line.getBufferSize());

				float oldVolume = (volume != null ? volume.getValue() : 0.0f);
				volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(oldVolume);
				volumeListener = new VolumeListener() {

					@Override
					public void volumeChanged(VolumeEvent ev) {
						volume.setValue(ev.getVolumeChange());

					}
				};
				if (audioPlayingListener != null) {
					AudioPlayingEvent ev = new AudioPlayingEvent(this, volumeListener);
					audioPlayingListener.AudioOn(ev);
				}
				
				// rawplay
				byte[] buffer = new byte[256];

				if (line != null) {
					// Start
					line.start();
					int nBytesRead = 0;
					System.out.println("Start rawplay loop");
					while (!bForceStop && nBytesRead != -1) {
						nBytesRead = audioInStream.read(buffer);
						if (nBytesRead != -1) {
							line.write(buffer, 0, nBytesRead);
						} else {
							System.out.println("-1!! No line write");
						}
					}
					// Stop
					System.out.println("End rawplay loop");
					line.drain();
					line.stop();
					line.close();
					audioInStream.close();
				}
				// end rawplay
//				in.close();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}
}
