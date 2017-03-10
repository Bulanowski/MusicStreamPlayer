package controller;

import view.ChatBoxView;
import view.ChatSendEvent;
import view.ChatSendListener;
import view.PrimaryView;

public class ChatBoxController {
	
	private ChatBoxView chatBoxView;
	
	public ChatBoxController(PrimaryView primaryView, TCPController tcpCtrl) {
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
