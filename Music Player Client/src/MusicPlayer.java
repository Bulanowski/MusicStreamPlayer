import controller.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.PrimaryView;

public class MusicPlayer extends Application {

	private PrimaryView primaryView;
	private MainController mc;

	public static void main(String[] args) {
		launch();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryView = new PrimaryView(primaryStage);
		mc = new MainController(primaryView);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				mc.onApplicationClosed();
			}
		});
	}

}
