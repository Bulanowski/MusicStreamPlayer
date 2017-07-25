package controller;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import model.Song;
import view.PrimaryView;
import view.SlideTabPane;

class SlideTabPaneController {
        Tab queueTab;
        ListChangeListener listener;
	
	public SlideTabPaneController(PrimaryView primaryView) {
		SlideTabPane slidePane = new SlideTabPane();
		primaryView.setRight(slidePane);
		slidePane.setContent(innerTabPane());

		listener = change -> {
                StringBuilder str = new StringBuilder();
                for (Pair<Integer, Song> pair: (ObservableList<Pair<Integer,Song>>)change.getList()) {
                    str.append(pair.getValue().getName() +" - "+pair.getValue().getArtist()+"\n");
                }
                setQueueTab(new Text(str.toString()));
        };
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

	public void setQueueTab(Node node) {
	    Platform.runLater(() -> queueTab.setContent(node));
    }
}
