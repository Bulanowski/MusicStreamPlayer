package model;

public class CommandSender implements Runnable {

	private Thread thread;
	private final TCP tcp;
	private final Distributor distributor;

	public CommandSender(TCP tcp, Distributor distributor) {
		this.tcp = tcp;
		this.distributor = distributor;
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

    public void requestQueue() {
        tcp.sendCommand("request_queue");
    }

    public void voteToRemove(int key) {
	    tcp.sendCommand("vote_to_remove "+key);
    }

    public void voteToSkip() {
        tcp.sendCommand("vote_to_skip");
    }

	private void songEnd() {
		tcp.sendCommand("song_end");
	}

	private void start() {
		if (thread == null) {
			thread = new Thread(this, "CommandSender");
			thread.start();
			System.out.println("Starting " + thread.getName() + " Thread");
		}
	}

	@Override
	public void run() {
		try {
			while (thread != null) {
				Object objectReceived = distributor.getFromQueue(PackageType.COMMAND);
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
			}
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " was interrupted");
		}
	}

	public void stop() {
		if (thread != null) {
			System.out.println("Stopping " + thread.getName() + " Thread");
			thread.interrupt();
			thread = null;
		}
	}

}
