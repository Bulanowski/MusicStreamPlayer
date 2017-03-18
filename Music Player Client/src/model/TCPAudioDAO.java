package model;

import java.nio.ByteBuffer;

public class TCPAudioDAO implements AudioDAO {
	
	private TCP tcp;
	private ByteBuffer byteBuffer;
	private byte[] audioBuffer;
	private volatile boolean playing = false;
	private int size = 0;
	private int bytesRead = 0;
	
	public TCPAudioDAO(TCP tcp) {
		this.tcp = tcp;
		tcp.addPackageReceivedListener(new PackageReceivedListener() {
			
			@Override
			public void readPackage(PackageReceivedEvent ev) {
				if (ev.getPackageType() == PackageType.BUFFER_SIZE.getByte()) {
					if (!playing) {
						size = (int) ev.getInformation();
						audioBuffer = new byte[size + 10];
						byteBuffer = ByteBuffer.wrap(audioBuffer);
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
					if (!playing && bytesRead >= size) {
						startPlaying();
					}
				}
			}
		});
	}
	
	public void reset() {
		playing = false;
		size = 0;
		bytesRead = 0;
	}
	
	public byte[] getAudioBuffer() {
		System.out.println("Get Audio Buffer");
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
		reset();
		tcp.sendCommandWithoutReturn("song_end");
	}
}
