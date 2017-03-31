package view;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SlideTabPane extends TabPane {
	private final Tab openTab;
	private Node innerNode;
	private boolean isOpen;

	public SlideTabPane() {
		openTab = new Tab();
		Label theOpenTab = new Label("<");
		openTab.setGraphic(theOpenTab);
		isOpen = false;
		openTab.setClosable(false);
		this.setSide(Side.LEFT);
		this.setMinWidth(30);
		this.getTabs().add(openTab);

		theOpenTab.setOnMouseClicked(value -> {
			if (isOpen) {
				this.setMinWidth(30);
				this.setPrefWidth(30);
				openTab.setContent(null);
				isOpen = false;
			} else {
				this.setMinWidth(300);
				openTab.setContent(innerNode);
				isOpen = true;
			}
		});
	}

	public void setContent(Node innerNode) {
		this.innerNode = innerNode;
	}

}
