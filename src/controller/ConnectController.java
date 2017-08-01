package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import model.AudioPlayer;
import model.CommandSender;
import model.TCP;
import view.ConnectView;
import view.PrimaryView;

import java.io.*;
import java.util.ArrayList;

public class ConnectController {
    private static ConnectView cv;
    private final PrimaryView primaryView;
    private static TCP tcp;
    private static CommandSender commandSender;
    private static AudioPlayer audioPlayer;
    private static MainController mainController;

    public ConnectController(PrimaryView primaryView, TCP tcp, CommandSender commandSender, AudioPlayer audioPlayer, MainController mainController) {
        cv = new ConnectView();

        ConnectController.tcp = tcp;
        ConnectController.audioPlayer = audioPlayer;
        ConnectController.commandSender = commandSender;
        ConnectController.mainController = mainController;

        this.primaryView = primaryView;

        getAddressesFromFile();

        cv.setConnectButtonAction( actionEvent -> {
            try {
                int PORT = 53308;
                if(cv.getUsername() != null && cv.getIP() != null) {
                    if (tcp.connect(cv.getIP(), PORT)) {
                        tcp.start();
                        commandSender.requestSongs();
                        commandSender.username(cv.getUsername());
//                    Random rand = new Random();
//                    commandSender.username("guest" + (100000 + rand.nextInt(899999)));
                        audioPlayer.start();
                        cv.addAddress(new Pair<>(cv.getIP(), cv.getUsername()));
                        saveList();
                        mainController.showMainView();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    public void getAddressesFromFile() {
        try {
            FileInputStream fis = new FileInputStream("savedAddresses");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            ArrayList<Pair<String,String>> list = (ArrayList) o;
            cv.addAddresses(list);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveList() {
        ArrayList<Pair> list = new ArrayList();


        try {
            FileInputStream fis = new FileInputStream("savedAddresses");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Pair> temp = (ArrayList) ois.readObject();

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {

            FileOutputStream fos = new FileOutputStream("savedAddresses");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Pair pair: cv.getSavedAddresses()) {

                boolean same = false;
                System.out.println("Starting...");
                for (Pair pair2:list) {

                    System.out.println(pair+ " : "+pair2);

                    if(pair.getKey().equals(pair2.getKey())&&pair.getValue()!=null) {
                        same = true;
                        if(!pair.getValue().equals(pair2.getValue())) {
                            System.out.println("KEY SAME");
                            list.remove(pair2);
                            list.add(pair);
                        }
                    }
                }
                if(!same) {
                    System.out.println(pair);
                    System.out.println("NOT SAME");
                    list.add(pair);
                }
            }
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setAsCenter() {
        primaryView.setCenter(cv);
    }

    public static class ConnectCell extends ListCell<Pair<String,String>> {
        VBox box = new VBox(10);
        Button connect;

        @Override
        public void updateItem(Pair<String,String> item, boolean empty) {
            super.updateItem(item, empty);
            Text username = new Text();
            Text ip = new Text();
            HBox buttons = new HBox(5);

            if (item != null) {
                username.setText(item.getValue());
                ip.setText(item.getKey());

                Label iplabel = new Label("Address: "+ip.getText());
                Label usernameLabel = new Label("Username: "+username.getText());
                usernameLabel.setPadding(new Insets(10,0,10,0));
                connect = new Button("Connect");
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
                            box.getChildren().addAll(iplabel, usernameLabel, buttons);
                        } else {
                            usernameField.getStyleClass().add("badField");
                        }
                    });


                    box.getChildren().clear();
                    buttons.getChildren().clear();
                    buttons.getChildren().addAll(done);
                    userBox.getChildren().addAll(usernameLabel,usernameField);
                    box.getChildren().addAll(iplabel,userBox,buttons);
                });


                buttons.getChildren().addAll(edit,connect);
                box.getChildren().addAll(iplabel,usernameLabel,buttons);
                setGraphic(box);

                connect.setOnAction((one) -> {
                    int PORT = 53308;
                    if (tcp.connect(ip.getText(), PORT)) {
                        tcp.start();
                        commandSender.requestSongs();
                        commandSender.username(username.getText());
                        cv.addAddress(new Pair<>(ip.getText(), username.getText()));
                        audioPlayer.start();
                        saveList();
                        mainController.showMainView();
                    }
                });
            }

        }
    }

}
