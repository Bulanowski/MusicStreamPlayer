package controller;

import java.util.Optional;
import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import model.AudioPlayer;
import model.CommandSender;
import model.SongModel;
import model.TCP;
import model.TableFilter;
import view.ConnectDialog;
import view.MenuView;
import view.PrimaryView;
import view.UsernameDialog;

public class MenuController {

	private MenuView menuView;

	public MenuController(PrimaryView primaryView, TreeController treeCtrl, TableController tableCtrl,
			StatusController statusCtrl, CommandSender commandCtrl, ChatController chatBoxCtrl, TCP tcp,
			SongModel songModel, AudioPlayer audioPlayer) {

		menuView = new MenuView();

		menuView.setOnConnectClickEvent(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				ConnectDialog connectDialog = new ConnectDialog();

				Optional<String> connectResult = connectDialog.showAndWait();
				if (connectResult.isPresent()) {
					try {
						if (tcp.connect(connectResult.get(), 53308)) {
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
				}
			};
		});

		menuView.setOnDisconnectClickEvent(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
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
					menuView.swapConnect();
				}
			}
		});

		menuView.setOnChatClickEvent(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (tcp.isConnected()) {
					chatBoxCtrl.show();
				} else {
					Alert alert = new Alert(AlertType.ERROR, "Unable to open chat while disconnected.", ButtonType.OK);
					alert.showAndWait();
				}
			}

		});

		menuView.addSearchChangedListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				tableCtrl.applyFilter(newValue, TableFilter.ALL);
			}
		});

		primaryView.setTop(menuView.getNode());

	}

}
