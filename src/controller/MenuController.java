package controller;

import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import model.AudioPlayer;
import model.SongModel;
import model.TCP;
import model.TableFilter;
import model.event_handling.SearchChangedEvent;
import model.event_handling.SearchChangedListener;
import view.ConnectDialog;
import view.MenuView;
import view.PrimaryView;

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

				Optional<Pair<String, String>> result = connectDialog.showAndWait();
				result.ifPresent(ipUser -> {
					try {
						if (tcp.connect(ipUser.getKey(), 53308)) {
							tcp.start();
							commandCtrl.requestSongs();
							commandCtrl.username(ipUser.getValue());
							audioPlayer.start();
							menuView.swapConnect();
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}

				});

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
				chatBoxCtrl.show();
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
