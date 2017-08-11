package controller;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import javafx.scene.text.Text;
import javafx.util.Pair;
import model.Song;
import view.PrimaryView;
import view.TabView;

class TabViewController {
    private ListChangeListener listener;
    private PrimaryView primaryView;
    private EventHandler<MouseEvent> show;
    private EventHandler<MouseEvent> hide;
    private TabView tabView;

	public TabViewController(PrimaryView primaryView) {
	    this.primaryView  = primaryView;
		tabView = new TabView();

		listener = change -> {
                StringBuilder str = new StringBuilder();
                for (Pair<Integer, Song> pair: (ObservableList<Pair<Integer,Song>>)change.getList()) {
                    str.append(pair.getValue().getName() +" - "+pair.getValue().getArtist()+"\n");
                }
            Platform.runLater(() -> tabView.setQueueTabContent(new Text(str.toString())));
        };


        show = event -> {
            primaryView.remove(tabView.getArrow());
            primaryView.setRight(tabView.getTabPane(0));
            tabView.setArrowAction(hide);
        };

        hide = event -> {
            primaryView.remove(tabView.getTabPane(1));
            primaryView.setRight(tabView.getArrow());
            tabView.setArrowAction(show);
        };

        tabView.setArrowAction(show);

	}

	public ListChangeListener getListListener() {
	    return listener;
    }
	
	public void setRight() {
	    primaryView.setRight(tabView.getArrow());
    }

    public void setChatContent(Node node) {
	    tabView.setChatTabContent(node);
    }

}
