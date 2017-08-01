package view;

import controller.ConnectController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;

import static controller.ConnectController.*;


/**
 * Created by phil on 7/14/17.
 */
public class ConnectView  extends VBox {
    private Button connect;
    private TextField ipField;
    private TextField usernameField;
    private ObservableList<Pair<String, String>> savedAddresses;
    private ListView oldConnections;


    public ConnectView() {
        this.setSpacing(20);
        this.setAlignment(Pos.TOP_CENTER);

        VBox center = new VBox(30);
        center.setAlignment(Pos.CENTER);

        Label welcome = new Label("Welcome! Connect to server below");
        welcome.setFont(Font.font(20));
        welcome.setPadding(new Insets(60));


        HBox ipBox = new HBox(10);
        ipBox.setAlignment(Pos.CENTER);
        Label ip = new Label("Enter IP address:");
        ipField = new TextField();
        ipBox.getChildren().addAll(ip,ipField);


        HBox usernameBox = new HBox(10);
        usernameBox.setAlignment(Pos.CENTER);
        Label username = new Label("Enter a username:");
        usernameField = new TextField();
        usernameBox.getChildren().addAll(username,usernameField);


        connect = new Button("Connect");

        Label oldConnectionsTitle = new Label("Past Connections:");
        oldConnectionsTitle.setFont(Font.font(20));
        oldConnectionsTitle.setPadding(new Insets(40,0,0,0));
        oldConnections = new ListView();

        oldConnections.setPlaceholder(new Label("No Past Connections."));

        oldConnections.setCellFactory( (one) -> new ConnectCell());



        savedAddresses = FXCollections.observableArrayList();
        oldConnections.setItems(savedAddresses);



        center.getChildren().addAll(ipBox,usernameBox, connect);
        this.getChildren().addAll(welcome,center, oldConnectionsTitle,oldConnections);
    }

    public void addAddresses(ArrayList<Pair<String,String>> list) {
            savedAddresses.addAll(list);
    }

    public void addAddress(Pair pair) {
        if(!savedAddresses.contains(pair)) {
            savedAddresses.add(pair);
        }
    }

    public ArrayList<Pair> getSavedAddresses() {
        return new ArrayList<Pair>(savedAddresses);
    }

    public void setConnectButtonAction(EventHandler<ActionEvent> eventHandler) {
        connect.setOnAction(eventHandler);
    }

    public String getUsername() {
        if(usernameField.getText().isEmpty()) {
            getIP();
            usernameField.getStyleClass().add("badField");
            return null;
        } else {
            if(usernameField.getText().length() < 3) {
                usernameField.getStyleClass().add("badField");
                return null;
            } else {
                usernameField.getStyleClass().remove("badField");
                return usernameField.getText();
            }
        }
    }

    public String getIP() {
        if(ipField.getText().isEmpty()) {
            ipField.getStyleClass().add("badField");
            return null;
        } else {
            ipField.getStyleClass().remove("badField");
            return ipField.getText();
        }
    }

}
