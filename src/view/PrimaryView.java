package view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PrimaryView {
	private final BorderPane pane;

	public PrimaryView(Stage primaryStage) {
		pane = new BorderPane();
		primaryStage.setScene(new Scene(pane, 1000, 800));
		primaryStage.setTitle("Teamspeak Music Player");
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(400);
		primaryStage.show();
	}

	public DoubleBinding getLeftWidth() {
		return pane.widthProperty().divide(5);
	}

	public void setLeft(Node left) {
		pane.setLeft(left);
	}

	public void setCenter(Node center) {
		pane.setCenter(center);
	}

	public void setRight(Node right) {
		pane.setRight(right);
	}

	public void setBottom(Node bottom) {
		pane.setBottom(bottom);
	}

	public void setTop(Node top) {
		pane.setTop(top);
	}
}
