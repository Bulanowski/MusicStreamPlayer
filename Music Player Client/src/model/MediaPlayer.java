package model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javazoom.jl.player.Player;

public class MediaPlayer extends Thread {

	Socket socket;
	Thread playback;
	Thread capture;
	Player player;
	InputStream socketInputStream;
	volatile byte[] audioBuffer = new byte[100000000];
	private volatile boolean startPlaying = false;
	ByteBuffer byteBuffer = ByteBuffer.wrap(audioBuffer);

	public MediaPlayer(String ipAddress) {
		try {
			socket = new Socket(ipAddress, 8796);
			capture = new Thread(new Capture());
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
					ByteArrayInputStream bais = new ByteArrayInputStream(audioBuffer);
					player = new Player(bais);
					Thread.sleep(1000);
					System.out.println("Playing");
					player.play();
					player.close();
					startPlaying = false;
				} else {
					audioBuffer = new byte[100000000];
					byteBuffer = ByteBuffer.wrap(audioBuffer);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class Capture extends Thread {

		public void run() {
			try {
				int PACKET_SIZE = 20;

				byte[] buffer = new byte[PACKET_SIZE];

				socketInputStream = socket.getInputStream();

				while (!this.isInterrupted()) {
					// while (!new String(buffer).equals("end")) {
					if (socketInputStream.available() >= buffer.length) {
						socketInputStream.read(buffer);
						byteBuffer.put(buffer);
						startPlaying = true;
					}
					// }
					// System.out.println("Stop Playing");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onApplicationClosed() {
		try {
			if(capture != null) {
			capture.interrupt();
			}
			if(socketInputStream != null) {
			socketInputStream.close();
			}
			if(socket != null) { 
			socket.close();
			}
			if(player != null) {
			player.close();
			}
			this.interrupt();
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
