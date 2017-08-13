package model;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Distributor {

	private final ArrayList<LinkedBlockingQueue<Object>> queueList = new ArrayList<>();

	public Distributor() {
		for (PackageType p : PackageType.values()) {
			queueList.add(p.getByte(), new LinkedBlockingQueue<>(50));
		}
	}

	public void addToQueue(PackageType type, Object object) throws InterruptedException {
		queueList.get(type.getByte()).put(object);
	}

	public Object getFromQueue(PackageType packageType) throws InterruptedException {
		return queueList.get(packageType.getByte()).take();
	}

}