package view;

import java.util.EventListener;

public interface ChatSendListener extends EventListener {
	
	void chatSend(ChatSendEvent event);

}
