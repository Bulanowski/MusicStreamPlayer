import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.PrimaryView;

public class MusicPlayer extends Application {

	public static void main(String[] args) {
		launch();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		PrimaryView primaryView = new PrimaryView(primaryStage);
		MainController mc = new MainController(primaryView);
	}

}
