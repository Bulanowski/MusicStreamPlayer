package model;

import java.util.ArrayList;

public class ChatDAO implements Runnable, Observable<String> {

	private Thread thread;
	private final Distributor distributor;
	private final ArrayList<Observer<String>> observers = new ArrayList<>();
	private String chatMessage;

	public ChatDAO(Distributor distributor) {
		this.distributor = distributor;
		start();
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, "ChatDAO");
			thread.start();
			System.out.println("Starting " + thread.getName() + " Thread");
		}
	}

	@Override
	public void run() {
		while (thread != null) {
			try {
				Object objectReceived = distributor.getFromQueue(PackageType.CHAT);
				if (objectReceived instanceof String) {
					chatMessage = (String) objectReceived;
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
	public void register(Observer<String> o) {
		observers.add(o);
	}

	@Override
	public void unregister(Observer<String> o) {
		int i = observers.indexOf(o);
		observers.remove(i);
	}

	@Override
	public void notifyObservers() {
		for (Observer<String> o : observers) {
			o.update(chatMessage);
		}
	}

}
