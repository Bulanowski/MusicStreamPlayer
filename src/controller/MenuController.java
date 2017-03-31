package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.*;
import view.ConnectDialog;
import view.MenuView;
import view.PrimaryView;
import view.UsernameDialog;

import java.util.Optional;
import java.util.Random;

class MenuController {

	private final MenuView menuView;

	MenuController(PrimaryView primaryView, TreeController treeCtrl, TableController tableCtrl,
                   CommandSender commandCtrl, ChatController chatBoxCtrl, TCP tcp,
                   SongModel songModel, ChatModel chatModel, AudioPlayer audioPlayer) {

		menuView = new MenuView();

		menuView.setOnConnectClickEvent(event -> {

            ConnectDialog connectDialog = new ConnectDialog();

            Optional<String> connectResult = connectDialog.showAndWait();
            connectResult.ifPresent(consumer -> {
                try {
                    int PORT = 53308;
                    if (tcp.connect(connectResult.get(), PORT)) {
                        tcp.start();
                        commandCtrl.requestSongs();
                        UsernameDialog usernameDialog = new UsernameDialog();
                        Optional<String> usernameResult = usernameDialog.showAndWait();
                        if (usernameResult.isPresent()) {
                            commandCtrl.username(usernameResult.get());
                        } else {
                            Random rand = new Random();
                            commandCtrl.username("guest" + (100000 + rand.nextInt(899999)));
                        }
                        audioPlayer.start();
                        menuView.swapConnect();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            });
        });

		menuView.setOnDisconnectClickEvent(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, null, ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Are you sure you want to disconnect from the server?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (audioPlayer != null) {
                    audioPlayer.stop();
                }
                if (tcp != null) {
                    tcp.disconnect();
                }
                treeCtrl.clear();
                songModel.clear();
                chatModel.clear();
                menuView.swapConnect();
            }
        });

		menuView.setOnChatClickEvent(event -> {
            if (tcp.isConnected()) {
                chatBoxCtrl.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR, "Unable to open chat while disconnected.", ButtonType.OK);
                alert.showAndWait();
            }
        });

		menuView.addSearchChangedListener((observable, oldValue, newValue) -> tableCtrl.applyFilter(newValue, TableFilter.ALL));

		primaryView.setTop(menuView.getNode());

	}

}
