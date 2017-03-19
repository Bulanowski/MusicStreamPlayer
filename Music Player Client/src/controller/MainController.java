package controller;

import model.AudioDAO;
import model.AudioPlayer;
import model.AudioPlayingEvent;
import model.AudioPlayingListener;
import model.SongDAO;
import model.SongModel;
import model.TCP;
import model.TCPAudioDAO;
import model.TCPSongDAO;
import view.PrimaryView;

public class MainController {
	// private PrimaryView pv;
	private final TCP tcp;
	private final SongModel songModel;
	private final AudioPlayer audioPlayer;
	private final CommandController commandCtrl;
	private final TableController tableCtrl;
	private final TreeController treeCtrl;
	private final StatusController statusCtrl;
	private final ChatController chatBoxCtrl;
	private final MenuController menuCtrl;

	public MainController(PrimaryView primaryView) {
		// this.pv = primaryView;

		tcp = new TCP();
		SongDAO songDAO = new TCPSongDAO(tcp);
		AudioDAO audioDAO = new TCPAudioDAO(tcp);
		songModel = new SongModel(songDAO);
		audioPlayer = new AudioPlayer(audioDAO);

		audioPlayer.setAudioPlayingListener(new AudioPlayingListener() {
			@Override
			public void AudioOn(AudioPlayingEvent ev) {
				statusCtrl.setVolumeListener(ev.getVolumeListener());
			}
		});

		commandCtrl = new CommandController(tcp);
		tableCtrl = new TableController(primaryView, commandCtrl);
		treeCtrl = new TreeController(primaryView, tableCtrl);
		statusCtrl = new StatusController(primaryView);
		chatBoxCtrl = new ChatController(primaryView, commandCtrl);
		menuCtrl = new MenuController(primaryView, treeCtrl, tableCtrl, statusCtrl, chatBoxCtrl, tcp, songModel,
				audioPlayer);

		tableCtrl.addSongModelListChangeListener(songModel);
		treeCtrl.addSongModelListChangeListener(songModel);
		statusCtrl.addSongModelListChangeListener(songModel);
	}

	public void onApplicationClosed() {
		audioPlayer.stop();
		tcp.disconnect();
	}

}