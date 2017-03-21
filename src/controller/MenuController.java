package controller;

import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import model.AudioPlayer;
import model.SongModel;
import model.TCP;
import model.TableFilter;
import model.event_handling.SearchChangedEvent;
import model.event_handling.SearchChangedListener;
import view.ConnectDialog;
import view.MenuView;
import view.PrimaryView;
import view.UsernameDialog;

public class MenuController {

	private MenuView menuView;

	public MenuController(PrimaryView primaryView, TreeController treeCtrl, TableController tableCtrl,
			StatusController statusCtrl, CommandController commandCtrl, ChatController chatBoxCtrl, TCP tcp,
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
								commandCtrl.username("guest" + (int) (100000.0 + (Math.random() * 899999.0)));
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

		menuView.setSearchChangedListener(new SearchChangedListener() {

			@Override
			public void searchChanged(SearchChangedEvent event) {
				tableCtrl.applyFilter(event.getSearchText(), TableFilter.ALL);
			}

		});

		primaryView.setTop(menuView.getNode());

	}

}
