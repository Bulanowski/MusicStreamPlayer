package model;

import java.util.List;
import java.util.Observable;

public class SongDAO extends Observable implements Runnable {

	private Thread thread;
	private final Distributor distributor;

	public SongDAO(Distributor distributor) {
		this.distributor = distributor;
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, "SongDAO-Thread");
			thread.start();
			System.out.println("Starting " + thread.getName());
		}
	}

	@Override
	public void run() {
		try {
			while (!thread.isInterrupted()) {
				Object objectReceived = distributor.getFromQueue(PackageType.SONG_LIST);
				if (objectReceived instanceof List) {
					setChanged();
					notifyObservers(objectReceived);
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
}
