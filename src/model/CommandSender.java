package model;

public class CommandSender implements Runnable {
	
	private Thread thread;
	private final TCP tcp;
	private final Distributer distributer;
	
	public CommandSender(TCP tcp, Distributer distributer) {
		this.tcp = tcp;
		this.distributer = distributer;
		start();
	}
	
	public void requestSongs() {
		tcp.sendCommand("request_songs");
	}
	
	public void addToQueue(int index) {
		tcp.sendCommand("add_to_queue " + index);
	}
	
	public void chat(String chatMessage) {
		tcp.sendCommand("chat " + chatMessage);
	}
	
	public void username(String username) {
		tcp.sendCommand("username "+ username);
	}
	
	public void songEnd() {
		tcp.sendCommand("song_end");
	}
	
	public void start() {
		if (thread == null) {
			thread = new Thread(this, "CommandSender");
			thread.start();
			System.out.println("Starting " + thread.getName() + " Thread");
		}
	}

	@Override
	public void run() {
		while (thread != null) {
			try {
				Object objectReceived = distributer.getFromQueue(PackageType.COMMAND);
				if (objectReceived instanceof String) {
					String command = (String) objectReceived;
					switch (command) {
					case "requestSongs":
						requestSongs();
						break;
					case "songEnd":
						songEnd();
						break;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		if (thread != null) {
			System.out.println("Stopping " + thread.getName() + " Thread");
			thread = null;
		}
	}

}
