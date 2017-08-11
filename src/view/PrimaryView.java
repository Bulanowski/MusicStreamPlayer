package view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PrimaryView {
	private final BorderPane pane;
	private final StackPane stackPane;

	public PrimaryView(Stage primaryStage) {
		pane = new BorderPane();
		Scene scene = new Scene(pane, 1000, 800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Stream Music Player");
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(400);
		primaryStage.show();

        stackPane = new StackPane();

	}

	public DoubleBinding getLeftWidth() {
		return pane.widthProperty().divide(5);
	}

	public ReadOnlyDoubleProperty getWidthProperty() { return pane.widthProperty(); }

	public void setLeft(Node left) {
        stackPane.getChildren().add(left);
        stackPane.setAlignment(left,Pos.CENTER_LEFT);
        pane.setCenter(stackPane);
	}


    public void setCenter(Node center) {
        stackPane.getChildren().add(center);
        pane.setCenter(stackPane);
    }

    public void setRight(Node right) {
        stackPane.getChildren().add(right);
        stackPane.setAlignment(right,Pos.CENTER_RIGHT);
        pane.setCenter(stackPane);
    }

    public void remove(Node node) {
	    stackPane.getChildren().remove(node);
    }

	public void setBottom(Node bottom) {
		pane.setBottom(bottom);
	}

	public void setTop(Node top) {
		pane.setTop(top);
	}

	public void removeAll() { pane.getChildren().clear(); }
}
