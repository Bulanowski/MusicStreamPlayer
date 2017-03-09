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

public class MenuView {
	private HBox menuBar;
	private Label connectLabel;
	private TextField searchField;
	private SearchChangedListener searchChangedListener;

	public MenuView() {

		MenuBar connectBar = new MenuBar();

		Menu connect = new Menu();
		connectLabel = new Label("Connect");
		connect.setGraphic(connectLabel);
		connectBar.getMenus().add(connect);
		connectBar.setMinHeight(100/3);

		MenuBar searchBar = new MenuBar();

		Menu search = new Menu();
		searchField = new TextField();
		searchField.setOnKeyReleased(e -> {
			SearchChangedEvent ev = new SearchChangedEvent(this, searchField.getText());
			if (searchChangedListener != null) {
				searchChangedListener.searchChanged(ev);
			}
		});
		search.setGraphic(searchField);
		search.setStyle("-fx-background-color: transparent;");
		searchBar.getMenus().add(search);

		Region spacer = new Region();
		spacer.getStyleClass().add("menu-bar");
		HBox.setHgrow(spacer, Priority.SOMETIMES);
		menuBar = new HBox(connectBar, spacer, searchBar);
	}

	public void onClickEvent(EventHandler<MouseEvent> mouseEvent) {
		connectLabel.setOnMouseClicked(mouseEvent);
	}
	
	public void setSearchChangedListener(SearchChangedListener searchChangedListener) {
		this.searchChangedListener = searchChangedListener;
	}

	public Node getNode() {
		return menuBar;
	}
}
