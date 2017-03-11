package controller;

import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import model.SongModel;
import model.TCP;
import model.TCPSongDAO;
import model.TableFilter;
import view.MenuView;
import view.PrimaryView;
import view.SearchChangedEvent;
import view.SearchChangedListener;

public class MenuController {

	private MenuView menuView;

	public MenuController(PrimaryView primaryView, TreeController treeCtrl, TableController tableCtrl,
			StatusController statusCtrl, ChatController chatBoxCtrl, TCP tcp) {

		menuView = new MenuView();

		menuView.onConnectClickEvent(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				TextInputDialog connectDialog = new TextInputDialog();
				ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);

				connectDialog.getDialogPane().setHeaderText("Enter the IP you wish to connect to.");
				connectDialog.getDialogPane().setContentText("IP:");
				connectDialog.getDialogPane().getButtonTypes().set(0, connectButtonType);

				Optional<String> result = connectDialog.showAndWait();
				if (result.isPresent()) {
					try {
						SongModel songModel = new SongModel(new TCPSongDAO(tcp, result.get()));
						treeCtrl.updateArtists(songModel);
						tableCtrl.updateSongs(songModel);
						statusCtrl.updateCount(songModel);
					} catch (NullPointerException e) {

					}
				}
			};
		});

		menuView.onChatClickEvent(new EventHandler<MouseEvent>() {

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
