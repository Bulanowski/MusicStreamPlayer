package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class StatusView {
	private HBox statusBar;
	private Text numberOfItems;

	public StatusView() {
		statusBar = new HBox(10);
		statusBar.setAlignment(Pos.CENTER);
		statusBar.setPadding(new Insets(5, 5, 5, 5));
	}

	public void setSongCount(int size) {
		numberOfItems = new Text(size + " songs");
		statusBar.getChildren().clear();
		statusBar.getChildren().add(numberOfItems);
	}

	public Node getNode() {
		return statusBar;
	}
}
