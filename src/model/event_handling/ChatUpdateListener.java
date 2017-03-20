package model.event_handling;

import java.util.EventListener;

public interface ChatUpdateListener extends EventListener {
	
	public void chatUpdated(ChatUpdateEvent ev);
}
