package controller;

import model.TCP;
import view.ChatBoxView;
import view.ChatSendEvent;
import view.ChatSendListener;
import view.PrimaryView;

public class ChatController {

	private ChatBoxView chatBoxView;
	private String username;

	public ChatController(PrimaryView primaryView, TCP tcp) {
		chatBoxView = new ChatBoxView();

		chatBoxView.setChatSendListener(new ChatSendListener() {

			@Override
			public void chatSend(ChatSendEvent event) {
				tcp.sendCommand("chat " + event.getMessageText());
			}

		});
	}

	public void show() {
		chatBoxView.show();
	}

}
