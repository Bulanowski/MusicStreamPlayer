package controller;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import model.CommandSender;
import model.Song;
import view.PrimaryView;
import view.QueueView;
import view.TabView;

public class TabViewController {
    private ListChangeListener listener;
    private PrimaryView primaryView;
    private EventHandler<MouseEvent> show;
    private EventHandler<MouseEvent> hide;
    private TabView tabView;
    private QueueView queueView;


	public TabViewController(PrimaryView primaryView, CommandSender commandSender) {
	    this.primaryView  = primaryView;
		tabView = new TabView();
		queueView = new QueueView();

        tabView.setQueueTabContent(queueView.getListView());

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

        EventHandler<RemoveEvent> handler = event -> {
            commandSender.voteToRemove(event.key);
        };


        queueView.setRemoveOnAction(handler);
	}


    public static class RemoveEvent extends Event {
        int key;

        public RemoveEvent(EventType<? extends Event> eventType, int key ) {
            super(eventType);
            this.key = key;

        }
    }


    public void setListView(ObservableList<Pair<Integer,Song>> list) {
	    queueView.setListView(list);
    }
	
	public void setRight() {
	    primaryView.setRight(tabView.getArrow());
    }

    public void setChatContent(Node node) {
	    tabView.setChatTabContent(node);
    }

}
