package view;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class MenuView {
	private final HBox menuBar;
	private final Menu options;
    private final TextField searchField;
    private final MenuItem disconnectButton;


//	private final Menu connect;
//	private final Label connectLabel;

//	private final Label chatLabel;


	public MenuView() {

		MenuBar menusBar = new MenuBar();
		menusBar.setMinHeight(100 / 3);


		options = new Menu("Options");


//		connect = new Menu();
//		connectLabel = new Label("Connect");
//		connect.setGraphic(connectLabel);
//		menusBar.getMenus().add(connect);
//
		disconnectButton = new MenuItem("Disconnect");

		options.getItems().add(disconnectButton);

//		Menu chat = new Menu();
//		chatLabel = new Label("Chat");
//		chat.setGraphic(chatLabel);
		menusBar.getMenus().add(options);

		MenuBar searchBar = new MenuBar();

		Menu search = new Menu();
		searchField = new TextField();
		search.setGraphic(searchField);
		searchField.setPromptText("Search");
		search.setStyle("-fx-background-color: transparent;");
		searchBar.getMenus().add(search);

		Region spacer = new Region();
		spacer.getStyleClass().add("menu-bar");
		HBox.setHgrow(spacer, Priority.SOMETIMES);
		menuBar = new HBox(menusBar, spacer, searchBar);
	}

//	public void swapConnect() {
//		if (connect.getGraphic() instanceof Label) {
//			String text = ((Label) connect.getGraphic()).getText();
//			if (text.equals("Connect")) {
//				connect.setGraphic(disconnectLabel);
//			} else {
//				connect.setGraphic(connectLabel);
//			}
//		}
//	}

//	public void setOnConnectClickEvent(EventHandler<MouseEvent> mouseEvent) {
//		connectLabel.setOnMouseClicked(mouseEvent);
//	}
//
	public void setOnDisconnectClickEvent(EventHandler<ActionEvent> actionEvent) {
         disconnectButton.setOnAction(actionEvent);
	}
//
//	public void setOnChatClickEvent(EventHandler<MouseEvent> mouseEvent) {
//		chatLabel.setOnMouseClicked(mouseEvent);
//	}

	public void addSearchChangedListener(ChangeListener<String> listener) {
		searchField.textProperty().addListener(listener);
	}

	public HBox getNode() {
		return menuBar;
	}
}
