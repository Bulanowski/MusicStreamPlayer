package model;

import java.nio.ByteBuffer;

public class TCPAudioDAO implements AudioDAO {
	
	private final TCP tcp;
	private ByteBuffer byteBuffer;
	private byte[] audioBuffer;
	private volatile boolean playing = false;
	private int size = 0;
	private int bytesRead = 0;
	private final double BUFFER_PERCENTAGE = 0.05; // default = 0.05
	
	public TCPAudioDAO(TCP tcp) {
		this.tcp = tcp;
		tcp.addPackageReceivedListener(new PackageReceivedListener() {
			
			@Override
			public void readPackage(PackageReceivedEvent ev) {
				if (ev.getPackageType() == PackageType.BUFFER_SIZE.getByte()) {
					if (!playing) {
						bytesRead = 0;
						size = (int) ev.getInformation();
						audioBuffer = new byte[size];
						byteBuffer = ByteBuffer.wrap(audioBuffer);
						System.out.println("Receiving file with size " + size);
					} else {
						System.out.println("Tried to set size while playing audio.");
						System.out.println("Size: " + size);
						System.out.println("BytesRead: " + bytesRead);
						System.out.println("Information: " + ev.getInformation());
					}
				} else if (ev.getPackageType() == PackageType.AUDIO.getByte()) {
					byte[] buffer = (byte[]) ev.getInformation();
					bytesRead += buffer.length;
					byteBuffer.put(buffer);
					if (!playing && bytesRead >= (size * BUFFER_PERCENTAGE)) {
						startPlaying();
					}
				}
			}
		});
	}
	
	public byte[] getAudioBuffer() {
		return audioBuffer;
	}
	
	public boolean getPlaying() {
		return playing;
	}
	
	public void startPlaying() {
		System.out.println("Start playing");
		playing = true;
	}
	
	public void stopPlaying() {
		System.out.println("Stop playing");
		playing = false;
		tcp.sendCommand("song_end");
	}
}
