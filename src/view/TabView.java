package view;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class TabView {
	private final TabPane pane;
    private final Tab chat;
    private final Tab queue;
    private Button sliderArrow;
    private HBox arrowAndTabPane;
    private Rectangle transparentRect;

    public TabView() {
	    pane = new TabPane();
	    chat = new Tab("Chat");
	    queue = new Tab("Queue");

	    chat.setClosable(false);
        queue.setClosable(false);

	    pane.getTabs().addAll(chat,queue);



        //TODO: fix: When table scrollbar button is under arrow, the scrollbar button doesn't work
        transparentRect = new Rectangle(20,20);
        transparentRect.setFill(Color.TRANSPARENT);
        Polygon arrowLeft = new Polygon();


        //Arrow Shape pointing left
        arrowLeft.getPoints().addAll(
                0.0, 20.0,

                10.0, 10.0, 10.0,14.0,

                4.0,20.0,

                10.0, 26.0, 10.0, 30.0 );




        arrowLeft.setFill(Color.rgb(255, 252, 246, 0.549));
        transparentRect.setOnMouseEntered( event -> arrowLeft.setFill(Color.rgb(255, 252, 246, 0.7843)));

        transparentRect.setOnMouseExited( event -> arrowLeft.setFill(Color.rgb(255, 252, 246, 0.549)));

        sliderArrow = new Button();

        int size = 400; //primaryView.getWidthProperty().divide(3).intValue();
        pane.setPrefWidth(size);
        pane.setMinWidth(size);

        arrowAndTabPane = new HBox();
        arrowAndTabPane.setPrefWidth(size);
        arrowAndTabPane.setMaxWidth(size);
        arrowAndTabPane.setAlignment(Pos.CENTER);

        StackPane buttonStack = new StackPane();
        buttonStack.getChildren().addAll(arrowLeft,transparentRect);


        sliderArrow.setGraphic(buttonStack);
        sliderArrow.getStyleClass().add("transparentButton");


	}

	public HBox getTabPane(int num) {
        switch (num) {
            case 0:
                arrowAndTabPane.getChildren().addAll(sliderArrow, pane);
                return arrowAndTabPane;
            case 1:
                return arrowAndTabPane;
            default:
                return null;
        }
    }

    public Button getArrow() {
        arrowAndTabPane.getChildren().clear();
        return sliderArrow;
    }

    public void setArrowAction(EventHandler<MouseEvent> event) {
        transparentRect.setOnMouseClicked(event);
    }


	public void setChatTabContent(Node node) {
        chat.setContent(node);
    }

    public void setQueueTabContent(Node node) {
        queue.setContent(node);
    }


}
