package controller;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import model.Song;
import org.w3c.dom.css.RGBColor;
import view.PrimaryView;
import view.SlideTabPane;

class SlideTabPaneController {
    private Tab queueTab;
    private ListChangeListener listener;
    private PrimaryView primaryView;
    private Button slider;
    private EventHandler<MouseEvent> show;
    private EventHandler<MouseEvent> hide;

	public SlideTabPaneController(PrimaryView primaryView) {
	    this.primaryView  = primaryView;
		SlideTabPane slidePane = new SlideTabPane();
		slidePane.setContent(innerTabPane());

		listener = change -> {
                StringBuilder str = new StringBuilder();
                for (Pair<Integer, Song> pair: (ObservableList<Pair<Integer,Song>>)change.getList()) {
                    str.append(pair.getValue().getName() +" - "+pair.getValue().getArtist()+"\n");
                }
                setQueueTab(new Text(str.toString()));
        };

        //TODO: fix: When table scrollbar button is under arrow, the scrollbar button doesn't work
        Rectangle transparentRect = new Rectangle(20,20);
        transparentRect.setFill(Color.TRANSPARENT);
        Polygon polygon = new Polygon();


//Arrow Shape pointing left
        polygon.getPoints().addAll(
                0.0, 20.0,

                10.0, 10.0, 10.0,14.0,

                4.0,20.0,

                10.0, 26.0, 10.0, 30.0 );




        polygon.setFill(Color.rgb(255, 252, 246, 0.302));
        transparentRect.setOnMouseEntered( event -> polygon.setFill(Color.rgb(255, 252, 246, 0.6275)));

        transparentRect.setOnMouseExited( event -> polygon.setFill(Color.rgb(255, 252, 246, 0.302)));

        slider = new Button();

        TabPane pane = new TabPane();
        pane.setPrefWidth(300);
        pane.setMinWidth(300);
        Tab tab = new Tab("Tab");
        HBox box = new HBox();
        Text text = new Text("Text");
        box.getChildren().add(text);
        tab.setContent(box);
        pane.getTabs().add(tab);

        HBox boxy = new HBox();
        boxy.setPrefWidth(300);
        boxy.setMaxWidth(300);
        boxy.setAlignment(Pos.CENTER);

        show = event -> {
            primaryView.remove(slider);
            boxy.getChildren().addAll(slider, pane);
            primaryView.setRight(boxy);
            transparentRect.setOnMouseClicked(hide);
        };

        hide = event -> {
            primaryView.remove(boxy);
            boxy.getChildren().clear();
            primaryView.setRight(slider);
            transparentRect.setOnMouseClicked(show);
        };

        transparentRect.setOnMouseClicked(show);

        StackPane buttonStack = new StackPane();
        buttonStack.getChildren().addAll(polygon,transparentRect);


        slider.setGraphic(buttonStack);
        slider.getStyleClass().add("transparentButton");

	}

	public ListChangeListener getListListener() {
	    return listener;
    }
	
	//Just a quick demo of what you could put inside the slideTabPane
	private TabPane innerTabPane() {
		Tab chatTab;
//		Tab queueTab;
		TabPane inner = new TabPane();
		inner.setTabMinWidth(115);
		chatTab = new Tab("Chat");
		queueTab = new Tab("Queue");
		chatTab.setClosable(false);
		chatTab.setContent(new Label("Chat"));
		queueTab.setClosable(false);
		queueTab.setContent(new Text("Queue"));
		inner.getTabs().addAll(chatTab, queueTab);
		return inner;
	}

	public void setRight() {
	    primaryView.setRight(slider);
    }

	public void setQueueTab(Node node) {
	    Platform.runLater(() -> queueTab.setContent(node));
    }
}
