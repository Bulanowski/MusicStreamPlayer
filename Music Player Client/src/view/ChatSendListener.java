package view;

import java.util.EventListener;

public interface ChatSendListener extends EventListener {
	
	public void chatSend(ChatSendEvent event);

}
