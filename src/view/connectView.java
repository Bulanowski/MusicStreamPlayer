package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by phil on 7/14/17.
 */
public class connectView {
    VBox center = new VBox();

    public void ConnectView(PrimaryView primaryView) {
        center.setAlignment(Pos.CENTER);
        HBox enterNew = new HBox();
        Label enter = new Label("Enter");
        TextField enterField = new TextField();
        enterNew.getChildren().addAll(enter,enterField);

    }
}
