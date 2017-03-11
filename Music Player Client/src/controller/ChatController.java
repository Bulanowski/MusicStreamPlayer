package controller;

import view.ChatBoxView;
import view.ChatSendEvent;
import view.ChatSendListener;
import view.PrimaryView;

public class ChatController {
	
	private ChatBoxView chatBoxView;
	private String name;
	
	public ChatController(PrimaryView primaryView, TCPController tcpCtrl) {
		chatBoxView = new ChatBoxView();
		
		chatBoxView.setChatSendListener(new ChatSendListener() {

			@Override
			public void chatSend(ChatSendEvent event) {
				tcpCtrl.sendCommand("chat " + event.getMessageText());
			}
			
		});
	}
	
	public void show() {
		chatBoxView.show();
	}

}
