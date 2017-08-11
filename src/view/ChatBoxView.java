package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ChatBoxView {
	
	private final BorderPane pane;
	private final TextArea chatArea;
	private final TextField messageField;
	private ChatSendListener chatSendListener;
	
	public ChatBoxView() {
		pane = new BorderPane();
		

		chatArea = new TextArea();
		chatArea.setEditable(false);
		pane.setCenter(chatArea);
		
		HBox messageBox = new HBox(10);
		messageBox.setPadding(new Insets(5));
		
		messageField = new TextField();
		messageField.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.ENTER)
				sendChat();
		});
		HBox.setHgrow(messageField, Priority.SOMETIMES);
		
		Button sendButton = new Button("Send");
		sendButton.setOnAction(e -> sendChat());
		
		messageBox.getChildren().addAll(messageField, sendButton);
		
		pane.setBottom(messageBox);
		
	}

	public BorderPane getNode() {
	    return pane;
    }

	private void sendChat() {
		ChatSendEvent event = new ChatSendEvent(this, messageField.getText());
		if (chatSendListener != null) {
			chatSendListener.chatSend(event);
			messageField.clear();
		}
	}
	
	public void setChatSendListener(ChatSendListener chatSendListener) {
		this.chatSendListener = chatSendListener;
	}
	
	public void addMessage(String messageText) {
		chatArea.appendText("\r\n" + messageText);
	}
	

}
