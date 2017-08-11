package controller;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.BorderPane;
import model.ChatModel;
import model.CommandSender;
import view.ChatBoxView;

class ChatController {

    private final ChatBoxView chatBoxView;

    ChatController(CommandSender commandCtrl) {
        chatBoxView = new ChatBoxView();
        chatBoxView.setChatSendListener(event -> commandCtrl.chat(event.getMessageText()));
    }

    public BorderPane getChatBoxNode() {
        return chatBoxView.getNode();
    }


    void addChatModelChangedListener(ChatModel chat) {
        chat.getChatList().addListener((ListChangeListener<String>) change -> chatBoxView.addMessage(chat.getChatList().remove(0)));
    }

}
