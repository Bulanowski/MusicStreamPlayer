package controller;

import javafx.stage.Stage;
import model.*;
import view.PrimaryView;


public class MainController {
	private final PrimaryView primaryView;
	private final TCP tcp;
	private final SongDAO songDAO;
	private final AudioDAO audioDAO;
	private final ChatDAO chatDAO;
	private final QueueDAO queueDAO;
	private final SongModel songModel;
	private final ChatModel chatModel;
	private final AudioPlayer audioPlayer;
	private final CommandSender commandCtrl;
	private final TableController tableCtrl;
	private final TreeController treeCtrl;
	private final StatusController statusCtrl;
	private final ChatController chatBoxCtrl;
	private final MenuController menuCtrl;
	private final TabViewController tabCtrl;
	private final ConnectController connectCtrl;

	public MainController(Stage primaryStage) {
		primaryView = new PrimaryView(primaryStage);

		Distributor distributor = new Distributor();
		tcp = new TCP(distributor);
		songDAO = new SongDAO(distributor);
        queueDAO = new QueueDAO(distributor);
		audioDAO = new AudioDAO(distributor,queueDAO);
		chatDAO = new ChatDAO(distributor);
		chatModel = new ChatModel(chatDAO);
		songModel = new SongModel(songDAO);
		audioPlayer = new AudioPlayer(audioDAO);

		queueDAO.setAudioDAO(audioDAO);

        audioDAO.setAudioPlayer(audioPlayer);

        commandCtrl = new CommandSender(tcp, distributor);
		tabCtrl = new TabViewController(primaryView,commandCtrl);
		tableCtrl = new TableController(primaryView, commandCtrl);
		treeCtrl = new TreeController(primaryView, tableCtrl);
		statusCtrl = new StatusController(primaryView,commandCtrl);
		chatBoxCtrl = new ChatController(commandCtrl);
		connectCtrl = new ConnectController(primaryView,tcp,commandCtrl,audioPlayer, this);

		menuCtrl = new MenuController(primaryView, this,treeCtrl, tableCtrl, commandCtrl, chatBoxCtrl, tcp,
				songModel, chatModel, audioPlayer);

		tabCtrl.setListView(queueDAO.getSongList());
        queueDAO.addListener(statusCtrl.getListListener());

		statusCtrl.addSongInfoChangeListener(audioDAO.getSongInfo());
		statusCtrl.addTrackPosition(audioDAO.getTrackLengthAndPosition());
		statusCtrl.addVolumeListener(audioPlayer.getVolumeChangeListener());

		tableCtrl.addSongModelListChangeListener(songModel);
		treeCtrl.addSongModelListChangeListener(songModel);
		statusCtrl.addSongModelListChangeListener(songModel);
		chatBoxCtrl.addChatModelChangedListener(chatModel);

        tabCtrl.setChatContent(chatBoxCtrl.getChatBoxNode());

        showConnectView();

	}

	public void showConnectView() {
        primaryView.removeAll();
        connectCtrl.setAsCenter();
    }

    public void showMainView() {
        primaryView.removeAll();
        menuCtrl.setAsTop();
        tableCtrl.setAsCenter();
        statusCtrl.setAsBottom();
        tabCtrl.setRight();
    }


	public void onApplicationClosed() {
		audioPlayer.stop();
		tcp.disconnect();
		commandCtrl.stop();
		queueDAO.stop();
		chatDAO.stop();
		audioDAO.stop();
		songDAO.stop();
	}
}