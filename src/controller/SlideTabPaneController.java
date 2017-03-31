package controller;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import view.PrimaryView;
import view.SlideTabPane;

class SlideTabPaneController {
	
	public SlideTabPaneController(PrimaryView primaryView) {
		SlideTabPane slidePane = new SlideTabPane();
		primaryView.setRight(slidePane);
		slidePane.setContent(innerTabPane());
	}
	
	
	//Just a quick demo of what you could put inside the slideTabPane
	private TabPane innerTabPane() {
		Tab chatTab;
		Tab queueTab;
		TabPane inner = new TabPane();
		inner.setTabMinWidth(115);
		chatTab = new Tab("Chat");
		queueTab = new Tab("Queue");
		chatTab.setClosable(false);
		chatTab.setContent(new Label("Chat Tab"));
		queueTab.setClosable(false);
		queueTab.setContent(new Label("Queue Tab"));
		inner.getTabs().addAll(chatTab, queueTab);
		return inner;
	}
}
