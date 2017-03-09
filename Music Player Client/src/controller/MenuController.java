package controller;

import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import model.SongModel;
import view.MenuView;
import view.PrimaryView;

public class MenuController {
	private MenuView menuView;

	public MenuController(PrimaryView primaryView, TCPController tcpCtrl, TreeController treeCtrl,
			TableController tableCtrl, StatusController statusCtrl) {

		menuView = new MenuView();

		menuView.onClickEvent(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				TextInputDialog connectDialog = new TextInputDialog();
				ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
				
				connectDialog.getDialogPane().getButtonTypes().set(0, connectButtonType);
				connectDialog.getDialogPane().setHeaderText("Enter the IP you wish to connect to.");
				connectDialog.getDialogPane().setContentText("IP:");
				
				Optional<String> result = connectDialog.showAndWait();
				if (result.isPresent()) {
					try {
						SongModel songModel = tcpCtrl.requestSongs(result.get());
						treeCtrl.updateArtists(songModel);
						tableCtrl.updateSongs(songModel);
						statusCtrl.updateCount(songModel);
					} catch (NullPointerException e) {

					}
				}
			};
		});

		primaryView.setTop(menuView.getNode());
	}
}
