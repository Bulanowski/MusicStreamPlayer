package model;

import java.util.EventListener;

public interface PackageReceivedListener extends EventListener {
	public void readPackage(PackageReceivedEvent ev);
}
