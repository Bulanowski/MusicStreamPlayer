package view;

import java.util.EventObject;

public class ChatSendEvent extends EventObject {

	private static final long serialVersionUID = -1592423892614794736L;
	private String messageText;

	public ChatSendEvent(Object source) {
		super(source);
	}

	public ChatSendEvent(Object source, String messageText) {
		super(source);
		this.messageText = messageText;
	}

	public String getMessageText() {
		return messageText;
	}

}
