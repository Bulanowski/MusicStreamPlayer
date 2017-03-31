package view;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class MenuView {
	private final HBox menuBar;
	private final Menu connect;
	private final Label connectLabel;
	private final Label disconnectLabel;
	private final Label chatLabel;
	private final TextField searchField;

	public MenuView() {

		MenuBar menusBar = new MenuBar();
		menusBar.setMinHeight(100 / 3);

		connect = new Menu();
		connectLabel = new Label("Connect");
		connect.setGraphic(connectLabel);
		menusBar.getMenus().add(connect);

		disconnectLabel = new Label("Disconnect");

		Menu chat = new Menu();
		chatLabel = new Label("Chat");
		chat.setGraphic(chatLabel);
		menusBar.getMenus().add(chat);

		MenuBar searchBar = new MenuBar();

		Menu search = new Menu();
		searchField = new TextField();
		search.setGraphic(searchField);
		search.setStyle("-fx-background-color: transparent;");
		searchBar.getMenus().add(search);

		Region spacer = new Region();
		spacer.getStyleClass().add("menu-bar");
		HBox.setHgrow(spacer, Priority.SOMETIMES);
		menuBar = new HBox(menusBar, spacer, searchBar);
	}

	public void swapConnect() {
		if (connect.getGraphic() instanceof Label) {
			String text = ((Label) connect.getGraphic()).getText();
			if (text.equals("Connect")) {
				connect.setGraphic(disconnectLabel);
			} else {
				connect.setGraphic(connectLabel);
			}
		}
	}

	public void setOnConnectClickEvent(EventHandler<MouseEvent> mouseEvent) {
		connectLabel.setOnMouseClicked(mouseEvent);
	}

	public void setOnDisconnectClickEvent(EventHandler<MouseEvent> mouseEvent) {
		disconnectLabel.setOnMouseClicked(mouseEvent);
	}

	public void setOnChatClickEvent(EventHandler<MouseEvent> mouseEvent) {
		chatLabel.setOnMouseClicked(mouseEvent);
	}

	public void addSearchChangedListener(ChangeListener<String> listener) {
		searchField.textProperty().addListener(listener);
	}

	public HBox getNode() {
		return menuBar;
	}
}
