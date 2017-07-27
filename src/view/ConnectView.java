package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by phil on 7/14/17.
 */
public class ConnectView {
    VBox center = new VBox();

    public ConnectView(PrimaryView primaryView) {
        center.setAlignment(Pos.CENTER);
        HBox enterNew = new HBox();
        enterNew.setAlignment(Pos.CENTER);
        Label enter = new Label("Enter IP address");
        TextField enterField = new TextField();
        enterNew.getChildren().addAll(enter,enterField);
        center.getChildren().addAll(enterNew);
        primaryView.setCenter(center);

    }
}
