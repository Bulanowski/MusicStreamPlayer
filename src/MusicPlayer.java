import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MusicPlayer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainController mc = new MainController(primaryStage);
        primaryStage.setOnCloseRequest(e -> mc.onApplicationClosed());
    }

    public static void main(String[] args) {
        launch();
    }
}
