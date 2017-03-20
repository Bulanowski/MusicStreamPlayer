package model;

import java.util.EventListener;

public interface ChatUpdateListener extends EventListener {
	
	public void chatUpdated(ChatUpdateEvent ev);
}
