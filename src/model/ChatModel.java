package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatModel implements Observer<String> {

	private final ChatDAO source;
	private final ObservableList<String> chatList;

	public ChatModel(ChatDAO chatDAO) {
		chatList = FXCollections.observableArrayList();
		source = chatDAO;
		source.register(this);
	}

	public void clear() {
		chatList.clear();
	}

	public ObservableList<String> getChatList() {
		return chatList;
	}

	@Override
	public void update(String e) {
		chatList.add(e);
	}

}
