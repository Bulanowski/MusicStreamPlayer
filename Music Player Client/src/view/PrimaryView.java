package view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PrimaryView {
	private Stage ps;
	private BorderPane bp;

	public PrimaryView(Stage primaryStage) {
		this.ps = primaryStage;

		bp = new BorderPane();

		primaryStage.setScene(new Scene(bp, 1000, 800));
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(400);
		primaryStage.show();
	}

	public DoubleBinding getLeftWidth() {
		return bp.widthProperty().divide(5);
	}

	public void setLeft(Node left) {
		bp.setLeft(left);
	}

	public void setCenter(Node center) {
		bp.setCenter(center);
	}

	public void setRight(Node right) {
		bp.setRight(right);
	}

	public void setBottom(Node bottom) {
		bp.setBottom(bottom);
	}

	public void setTop(Node top) {
		bp.setTop(top);

	}
}
