package model;

import java.util.ArrayList;
import java.util.List;

public class SongDAO implements Runnable, Observable<List<Song>> {

	private Thread thread;
	private final Distributer distributer;
	private final ArrayList<Observer<List<Song>>> observers = new ArrayList<>();
	private List<Song> songList;

	public SongDAO(Distributer distributer) {
		this.distributer = distributer;
		start();
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, "SongDAO");
			thread.start();
			System.out.println("Starting " + thread.getName() + " Thread");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (thread != null) {
			try {
				Object objectReceived = distributer.getFromQueue(PackageType.SONG_LIST);
				if (objectReceived instanceof List) {
					songList = (List<Song>) objectReceived;
					notifyObservers();
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

	@Override
	public void register(Observer<List<Song>> o) {
		observers.add(o);
	}

	@Override
	public void unregister(Observer<List<Song>> o) {
		int i = observers.indexOf(o);
		observers.remove(i);
	}

	@Override
	public void notifyObservers() {
		for (Observer<List<Song>> o : observers) {
			o.update(songList);
		}
	}
}
