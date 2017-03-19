package view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PrimaryView {
	private Stage stage;
	private BorderPane pane;

	public PrimaryView(Stage primaryStage) {
		stage = primaryStage;

		pane = new BorderPane();

		stage.setScene(new Scene(pane, 1000, 800));
		stage.setTitle("Teamspeak Music Player");
		stage.setMinHeight(200);
		stage.setMinWidth(400);
		stage.show();
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
