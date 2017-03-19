package controller;

import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import model.AudioPlayingEvent;
import model.AudioPlayingListener;
import model.AudioPlayer;
import model.SearchChangedEvent;
import model.SearchChangedListener;
import model.SongModel;
import model.TCP;
import model.TCPAudioDAO;
import model.TCPSongDAO;
import model.TableFilter;
import view.MenuView;
import view.PrimaryView;

public class MenuController {

	private MenuView menuView;
	private TCP tcp;
	private SongModel songModel;
	private AudioPlayer audioPlayer;

	public MenuController(PrimaryView primaryView, TreeController treeCtrl, TableController tableCtrl,
			StatusController statusCtrl, ChatController chatBoxCtrl, TCP tcp) {

		this.tcp = tcp;
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
						tcp.connect(result.get(), 53308);
						songModel = new SongModel(new TCPSongDAO(tcp));
						audioPlayer = new AudioPlayer(new TCPAudioDAO(tcp));
						tcp.start();

						audioPlayer.setAudioPlayingListener(new AudioPlayingListener() {

							@Override
							public void AudioOn(AudioPlayingEvent ev) {
								statusCtrl.setVolumeListener(ev.getVolumeListener());

							}
						});

						treeCtrl.updateArtists(songModel);
						tableCtrl.updateSongs(songModel);
						statusCtrl.updateCount(songModel);
					} catch (NullPointerException e) {
						e.printStackTrace();
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

	public void onApplicationClosed() {
		if (audioPlayer != null) {
			audioPlayer.onApplicationClosed();
		}
	}

}
