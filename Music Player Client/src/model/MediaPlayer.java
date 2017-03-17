package model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.player.Player;

public class MediaPlayer extends Thread {

	Socket socket;
	Playback playback;
	Thread capture;
	Player player;
	Line res;
	private FloatControl volume;
	private AudioPlayingListener audioPlayingListener;
	private VolumeListener volumeListener;
	InputStream socketInputStream;
	volatile byte[] audioBuffer;
	private volatile boolean startPlaying = false;
	private volatile boolean gotSize;
	ByteBuffer byteBuffer;

	public MediaPlayer(String ipAddress) {
		try {
			this.setName("Play-Thread");
			socket = new Socket(ipAddress, 8796);
			capture = new Thread(new Capture());
			capture.setName("Capture-Thread");
			capture.start();
			start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (!this.isInterrupted()) {
			try {
				if (startPlaying) {
					Thread.sleep(1000);
					System.out.println("Playing");
					playback = new Playback();
					playback.start();
					startPlaying = false;
				}
				Thread.sleep(500);

			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName() + " was interrupted");
			}
		}

	}

	public void setAudioPlayingListener(AudioPlayingListener audioPlayingListener) {
		this.audioPlayingListener = audioPlayingListener;
	}

	class Playback {

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
				while (nBytesRead != -1) {
					if (din.available() != -1) {
						nBytesRead = din.read(data, 0, data.length);
					} else {
						din.reset();
						nBytesRead = din.read(data, 0, data.length);
					}
					if (nBytesRead != -1) {
						line.write(data, 0, nBytesRead);
					} else {
						gotSize = false;
						System.out.println("-1!! No line write");
					}
				}
				// Stop
				line.drain();
				line.stop();
				line.close();
				din.close();
			}
		}

		public void start() {
			ByteArrayInputStream bais = new ByteArrayInputStream(audioBuffer);
			try {
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

	class Capture extends Thread {

		public void run() {
			int bytesRead = 0;
			try {
				int PACKET_SIZE = 10;

				byte[] buffer;
				socketInputStream = socket.getInputStream();
				gotSize = false;
				while (!this.isInterrupted()) {
					if (socketInputStream.available() >= PACKET_SIZE) {
						int size = 0;

						if (!gotSize) {
							buffer = new byte[4];
							socketInputStream.read(buffer);
							size = ByteBuffer.wrap(buffer).getInt();
							System.out.println(size);
							audioBuffer = new byte[size + 10];
							byteBuffer = ByteBuffer.wrap(audioBuffer);
						} else {
							buffer = new byte[PACKET_SIZE];
							bytesRead += socketInputStream.read(buffer);
							byteBuffer.put(buffer);
							if (bytesRead >= 4096) {
								startPlaying = true;
							}
						}
						gotSize = true;

					} else {
						Thread.sleep(500);
					}

				}
			} catch (IOException e) {
				if (!e.getMessage().equals("Stream closed.")) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName() + " was interrupted");
			}
		}
	}

	public void onApplicationClosed() {
		try {
			this.interrupt();
			if (capture != null) {
				capture.interrupt();
			}
			if (socket != null) {
				socket.close();
			}
			if (res != null) {
				((DataLine) res).drain();
				((DataLine) res).stop();
				res.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
