package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;

import controller.ConnectController.*;


/**
 * Created by phil on 7/14/17.
 */
public class ConnectView  extends VBox {
    private Button connect;
    private TextField ipField;
    private TextField usernameField;
    private ObservableList<Pair<String, String>> savedAddresses;
    private ListView oldConnections;
    private EventHandler handler;


    public ConnectView(EventHandler handler) {
        this.handler = handler;
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

        oldConnections.setCellFactory( (one) -> {
            ConnectCell cell = new ConnectCell();
            cell.addEventHandler(handler);
            return cell;
        });



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


    public static class ConnectCell extends ListCell<Pair<String,String>> {
        VBox box = new VBox(10);
        Button connect = new Button("Connect");
        static int id = 0;
        EventType eventConnect;

        public void addEventHandler(EventHandler handler) {
            EventType<ConnectEvent> connect = new EventType<>("connect-"+ id++);
            eventConnect = connect;
            this.connect.addEventHandler(connect, handler);

        }

        @Override
        public void updateItem(Pair<String,String> item, boolean empty) {

            super.updateItem(item, empty);
            Text username = new Text();
            Text ip = new Text();
            HBox buttons = new HBox(5);

            if (item != null) {
                username.setText(item.getValue());
                ip.setText(item.getKey());

                Label ipLabel = new Label("Address: "+ip.getText());
                Label usernameLabel = new Label("Username: "+username.getText());
                usernameLabel.setPadding(new Insets(10,0,10,0));
                Button edit = new Button("Edit");

                edit.setOnAction((one) -> {
                    usernameLabel.setText("Username: ");
                    HBox userBox = new HBox();
                    userBox.setAlignment(Pos.CENTER_LEFT);
                    TextField usernameField = new TextField();
                    usernameField.setText(username.getText());
                    usernameField.setPadding(new Insets(3,0,3,0));
                    Button done = new Button("Done");

                    done.setOnAction((two) -> {
                        if(!usernameField.getText().isEmpty() && usernameField.getText().length() > 2) {
                            usernameField.getStyleClass().remove("badField");
                            username.setText(usernameField.getText());
                            usernameLabel.setText("Username: " + username.getText());
                            box.getChildren().clear();
                            buttons.getChildren().clear();
                            buttons.getChildren().addAll(edit, connect);
                            box.getChildren().addAll(ipLabel, usernameLabel, buttons);
                        } else {
                            usernameField.getStyleClass().add("badField");
                        }
                    });


                    box.getChildren().clear();
                    buttons.getChildren().clear();
                    buttons.getChildren().addAll(done);
                    userBox.getChildren().addAll(usernameLabel,usernameField);
                    box.getChildren().addAll(ipLabel,userBox,buttons);
                });


                buttons.getChildren().addAll(edit,connect);
                box.getChildren().addAll(ipLabel,usernameLabel,buttons);
                setGraphic(box);

                connect.setOnAction((one) -> {
                    ConnectEvent event = new ConnectEvent(eventConnect,username.getText(), ip.getText());
                    connect.fireEvent(event);
                });
            }

        }
    }

}
