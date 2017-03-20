package controller;

import model.AudioDAO;
import model.AudioPlayer;
import model.ChatDAO;
import model.ChatModel;
import model.SongDAO;
import model.SongModel;
import model.TCP;
import model.TCPAudioDAO;
import model.TCPChatDAO;
import model.TCPSongDAO;
import model.event_handling.AudioPlayingEvent;
import model.event_handling.AudioPlayingListener;
import view.PrimaryView;

public class MainController {
	// private PrimaryView pv;
	private final TCP tcp;
	private final SongModel songModel;
	private final ChatModel chatModel;
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
		ChatDAO chatDAO = new TCPChatDAO(tcp);
		chatModel = new ChatModel(chatDAO);
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
		menuCtrl = new MenuController(primaryView, treeCtrl, tableCtrl, statusCtrl,commandCtrl ,chatBoxCtrl, tcp, songModel,
				audioPlayer);

		tableCtrl.addSongModelListChangeListener(songModel);
		treeCtrl.addSongModelListChangeListener(songModel);
		statusCtrl.addSongModelListChangeListener(songModel);
		chatBoxCtrl.addChatModelChangedListener(chatModel);
	}

	public void onApplicationClosed() {
		audioPlayer.stop();
		tcp.disconnect();
	}

}