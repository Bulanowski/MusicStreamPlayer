package model;

import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatModel implements Observer {

    private final ObservableList<String> chatList;

    public ChatModel(ChatDAO chatDAO) {
        chatList = FXCollections.observableArrayList();
        chatDAO.addObserver(this);
        chatDAO.start();
    }

    public void clear() {
        chatList.clear();
    }

    public ObservableList<String> getChatList() {
        return chatList;
    }

    @Override
    public void update(Observable o, Object obj) {
        chatList.add((String) obj);
    }

}
