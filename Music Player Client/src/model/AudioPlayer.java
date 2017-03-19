package model;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer implements Runnable {

	private Thread thread;
	private Playback playback;
	private Line res;
	private final AudioDAO audioDAO;
	private FloatControl volume;
	private AudioPlayingListener audioPlayingListener;
	private VolumeListener volumeListener;

	public AudioPlayer(AudioDAO audioDAO) {
		this.audioDAO = audioDAO;
		start();
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setName("Audio-Player");
			thread.start();
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
		thread = null;
	}

	public void setAudioPlayingListener(AudioPlayingListener audioPlayingListener) {
		this.audioPlayingListener = audioPlayingListener;
	}

	class Playback {

		private volatile boolean forceStop = false;

		public void forceStop() {
			forceStop = true;
		}

		private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
			float oldVolume = (volume != null ? volume.getValue() : 0.0f);
			res = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			res = (SourceDataLine) AudioSystem.getLine(info);
			res.open();
			volume = (FloatControl) res.getControl(FloatControl.Type.MASTER_GAIN);
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

			return (SourceDataLine) res;
		}

		private void rawplay(AudioFormat targetFormat, AudioInputStream din)
				throws IOException, LineUnavailableException {
			byte[] data = new byte[256];
			SourceDataLine line = getLine(targetFormat);
			if (line != null) {
				// Start
				line.start();
				int nBytesRead = 0;
				System.out.println("Start rawplay loop");
				while (!forceStop && nBytesRead != -1) {
					nBytesRead = din.read(data);
					if (nBytesRead != -1) {
						line.write(data, 0, nBytesRead);
					} else {
						System.out.println("-1!! No line write");
					}
				}
				// Stop
				System.out.println("End rawplay loop");
				line.drain();
				line.stop();
				line.close();
				din.close();
			}
		}

		public void start() {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(audioDAO.getAudioBuffer());
				AudioInputStream in = AudioSystem.getAudioInputStream(bais);
				AudioFormat baseFormat = in.getFormat();
				AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
						16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
				AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
				rawplay(decodedFormat, din);
				in.close();

			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void onApplicationClosed() {
		thread = null;
		if (playback != null) {
			playback.forceStop();
		}
		if (res != null) {
			((DataLine) res).drain();
			((DataLine) res).stop();
			res.close();
		}

	}

}
