package controller;

import javafx.collections.ListChangeListener;
import model.ChatModel;
import model.CommandSender;
import view.ChatBoxView;

class ChatController {

    private final ChatBoxView chatBoxView;

    ChatController(CommandSender commandCtrl) {
        chatBoxView = new ChatBoxView();
        chatBoxView.setChatSendListener(event -> commandCtrl.chat(event.getMessageText()));
    }

    void show() {
        chatBoxView.show();
    }

    void addChatModelChangedListener(ChatModel chat) {
        chat.getChatList().addListener((ListChangeListener<String>) change -> chatBoxView.addMessage(chat.getChatList().remove(0)));
    }

}
