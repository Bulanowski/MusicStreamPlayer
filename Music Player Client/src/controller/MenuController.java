package controller;

import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import model.AudioDAO;
import model.AudioPlayer;
import model.AudioPlayingEvent;
import model.AudioPlayingListener;
import model.SearchChangedEvent;
import model.SearchChangedListener;
import model.SongDAO;
import model.SongModel;
import model.TCP;
import model.TCPAudioDAO;
import model.TCPSongDAO;
import model.TableFilter;
import view.ConnectDialog;
import view.MenuView;
import view.PrimaryView;

public class MenuController {

	private MenuView menuView;
	private SongModel songModel;
	private AudioPlayer audioPlayer;

	public MenuController(PrimaryView primaryView, TreeController treeCtrl, TableController tableCtrl,
			StatusController statusCtrl, ChatController chatBoxCtrl, TCP tcp) {

		menuView = new MenuView();

		menuView.setOnConnectClickEvent(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				ConnectDialog connectDialog = new ConnectDialog();

				Optional<String> result = connectDialog.showAndWait();
				if (result.isPresent()) {
					try {
						tcp.connect(result.get(), 53308);
						SongDAO songDAO = new TCPSongDAO(tcp);
						AudioDAO audioDAO = new TCPAudioDAO(tcp);
						songModel = new SongModel(songDAO);
						audioPlayer = new AudioPlayer(audioDAO);
						tcp.start();

						audioPlayer.setAudioPlayingListener(new AudioPlayingListener() {

							@Override
							public void AudioOn(AudioPlayingEvent ev) {
								statusCtrl.setVolumeListener(ev.getVolumeListener());

							}
						});

						tableCtrl.addSongModelListChangeListener(songModel);
						treeCtrl.addSongModelListChangeListener(songModel);
						statusCtrl.addSongModelListChangeListener(songModel);
						songDAO.requestSongs();
						menuView.swapConnect();
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
					tcp.disconnect();
					treeCtrl.clear();
					songModel.clear();
					audioPlayer.onApplicationClosed();
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

	public void onApplicationClosed() {
		if (audioPlayer != null) {
			audioPlayer.onApplicationClosed();
		}
	}

}
