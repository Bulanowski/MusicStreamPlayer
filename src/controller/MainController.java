package controller;

import model.AudioDAO;
import model.AudioPlayer;
import model.ChatDAO;
import model.ChatModel;
import model.CommandSender;
import model.Distributer;
import model.SongDAO;
import model.SongModel;
import model.TCP;
import view.PrimaryView;

public class MainController {
	// private PrimaryView pv;
	private final TCP tcp;
	private final SongDAO songDAO;
	private final AudioDAO audioDAO;
	private final ChatDAO chatDAO;
	private final SongModel songModel;
	private final ChatModel chatModel;
	private final AudioPlayer audioPlayer;
	private final CommandSender commandCtrl;
	private final TableController tableCtrl;
	private final TreeController treeCtrl;
	private final StatusController statusCtrl;
	private final ChatController chatBoxCtrl;
	private final MenuController menuCtrl;
	private final SlideTabPaneController slideCtrl;

	public MainController(PrimaryView primaryView) {
		// this.pv = primaryView;

		Distributer distributer = new Distributer();
		tcp = new TCP(distributer);
		songDAO = new SongDAO(distributer);
		audioDAO = new AudioDAO(distributer);
		chatDAO = new ChatDAO(distributer);
		chatModel = new ChatModel(chatDAO);
		songModel = new SongModel(songDAO);
		audioPlayer = new AudioPlayer(audioDAO);

//		audioPlayer.setAudioPlayingListener(new AudioPlayingListener() {
//			@Override
//			public void AudioOn(AudioPlayingEvent ev) {
//				statusCtrl.setVolumeListener(ev.getVolumeListener());
//			}
//		});

		commandCtrl = new CommandSender(tcp, distributer);
		slideCtrl = new SlideTabPaneController(primaryView);
		tableCtrl = new TableController(primaryView, commandCtrl);
		treeCtrl = new TreeController(primaryView, tableCtrl);
		statusCtrl = new StatusController(primaryView);
		chatBoxCtrl = new ChatController(primaryView, commandCtrl);
		menuCtrl = new MenuController(primaryView, treeCtrl, tableCtrl, statusCtrl, commandCtrl, chatBoxCtrl, tcp,
				songModel, audioPlayer);

		statusCtrl.addVolumeListener(audioPlayer.getVolumeChangeListener());
		tableCtrl.addSongModelListChangeListener(songModel);
		treeCtrl.addSongModelListChangeListener(songModel);
		statusCtrl.addSongModelListChangeListener(songModel);
		chatBoxCtrl.addChatModelChangedListener(chatModel);
	}

	public void onApplicationClosed() {
		audioPlayer.stop();
		tcp.disconnect();
		commandCtrl.stop();
		chatDAO.stop();
		audioDAO.stop();
		songDAO.stop();
	}

}