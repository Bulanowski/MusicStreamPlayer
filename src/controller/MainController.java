package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import model.AudioDAO;
import model.AudioPlayer;
import model.ChatDAO;
import model.ChatModel;
import model.CommandSender;
import model.Distributor;
import model.SongDAO;
import model.SongModel;
import model.TCP;
import view.PrimaryView;

public class MainController extends Application{
	private final PrimaryView primaryView;
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

	public MainController(Stage primaryStage) {
		primaryView = new PrimaryView(primaryStage);

		Distributor distributor = new Distributor();
		tcp = new TCP(distributor);
		songDAO = new SongDAO(distributor);
		audioDAO = new AudioDAO(distributor);
		chatDAO = new ChatDAO(distributor);
		chatModel = new ChatModel(chatDAO);
		songModel = new SongModel(songDAO);
		audioPlayer = new AudioPlayer(audioDAO);

		commandCtrl = new CommandSender(tcp, distributor);
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

	@Override
	public void start(Stage primaryStage) throws Exception {
		new MainController(primaryStage);
		primaryStage.setOnCloseRequest(e -> onApplicationClosed());
	}

	public static void main(String[] args) {
		launch();
	}
}