package model;

import model.event_handling.ChatUpdateListener;

public interface ChatDAO {
	
	public void setChatUpdateListener(ChatUpdateListener chatUpateListener);
	
}
