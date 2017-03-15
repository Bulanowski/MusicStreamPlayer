package view;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import model.SearchChangedEvent;
import model.SearchChangedListener;

public class MenuView {
	private HBox menuBar;
	private Label connectLabel;
	private Label chatLabel;
	private TextField searchField;
	private SearchChangedListener searchChangedListener;

	public MenuView() {

		MenuBar menusBar = new MenuBar();
		menusBar.setMinHeight(100 / 3);

		Menu connect = new Menu();
		connectLabel = new Label("Connect");
		connect.setGraphic(connectLabel);
		menusBar.getMenus().add(connect);
		
		Menu chat = new Menu();
		chatLabel = new Label("Chat");
		chat.setGraphic(chatLabel);
		menusBar.getMenus().add(chat);

		MenuBar searchBar = new MenuBar();

		Menu search = new Menu();
		searchField = new TextField();
		searchField.setOnKeyReleased(e -> {
			SearchChangedEvent event = new SearchChangedEvent(this, searchField.getText());
			if (searchChangedListener != null) {
				searchChangedListener.searchChanged(event);
			}
		});
		search.setGraphic(searchField);
		search.setStyle("-fx-background-color: transparent;");
		searchBar.getMenus().add(search);

		Region spacer = new Region();
		spacer.getStyleClass().add("menu-bar");
		HBox.setHgrow(spacer, Priority.SOMETIMES);
		menuBar = new HBox(menusBar, spacer, searchBar);
	}

	public void onConnectClickEvent(EventHandler<MouseEvent> mouseEvent) {
		connectLabel.setOnMouseClicked(mouseEvent);
	}
	
	public void onChatClickEvent(EventHandler<MouseEvent> mouseEvent) {
		chatLabel.setOnMouseClicked(mouseEvent);
	}

	public void setSearchChangedListener(SearchChangedListener searchChangedListener) {
		this.searchChangedListener = searchChangedListener;
	}

	public Node getNode() {
		return menuBar;
	}
}
