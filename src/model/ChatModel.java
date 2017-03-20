package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.event_handling.ChatUpdateEvent;
import model.event_handling.ChatUpdateListener;

public class ChatModel {
	private ChatDAO chatDAO;
	private ObservableList<String> chatList;
	
	
	public ChatModel(ChatDAO chatDAO) {
		chatList = FXCollections.observableArrayList();
		this.chatDAO = chatDAO;
		
		chatDAO.setChatUpdateListener(new ChatUpdateListener() {
			
			@Override
			public void chatUpdated(ChatUpdateEvent ev) {
				chatList.add(ev.getMsg());
				
			}
		});
	}
	
	public ObservableList<String> getChatList() {
		return chatList;
	}
	
	
	
}