package controller;

import view.ChatBoxView;
import view.ChatSendEvent;
import view.ChatSendListener;
import view.PrimaryView;

public class ChatController {

	private ChatBoxView chatBoxView;
//	private String username;

	public ChatController(PrimaryView primaryView, CommandController commandCtrl) {
		chatBoxView = new ChatBoxView();

		chatBoxView.setChatSendListener(new ChatSendListener() {

			@Override
			public void chatSend(ChatSendEvent event) {
				commandCtrl.chat(event.getMessageText());
			}

		});
	}

	public void show() {
		chatBoxView.show();
	}

}
