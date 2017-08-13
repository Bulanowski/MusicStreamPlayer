package view;

import controller.TabViewController;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import model.Song;



public class QueueView {
    private final ListView<Pair<Integer,Song>> listView;
    private ObservableList<Pair<Integer,Song>> queueItems;
    private EventHandler handler;


    public QueueView() {
        listView = new ListView<>();
        listView.setCellFactory( callback -> {
            QueueCell cell = new QueueCell();
            cell.addEventHandler(handler);
            return cell;
        });
    }

    private void createQueueListener() {

        queueItems.addListener((ListChangeListener<? super Pair<Integer,Song>>)  listener -> {

        });

    }

    public void setListView(ObservableList<Pair<Integer,Song>> list) {
        queueItems = list;
        createQueueListener();
        listView.setItems(queueItems);
    }

    public ListView<Pair<Integer,Song>> getListView() {
        return listView;
    }

    public void setRemoveOnAction(EventHandler handler) {
        this.handler = handler;
    }

    public static class QueueCell extends ListCell<Pair<Integer,Song>> {
        VBox box;
        Label songInfo;
        Button remove = new Button("Remove");
        EventType eventRemove;
        static int id = 0;

        public void addEventHandler(EventHandler handler) {
            EventType<TabViewController.RemoveEvent> remove = new EventType<>("remove-"+ id++);
            eventRemove = remove;
            this.remove.addEventHandler(remove, handler);

        }

        @Override
        protected void updateItem(Pair<Integer, Song> item, boolean empty) {
            super.updateItem(item, empty);


            if(empty || item == null) {
                setGraphic(null);
                setText(null);
                this.getStyleClass().add("transparentCell");
            }


            songInfo = new Label();
            box = new VBox(10);
            if(item != null && item.getValue() != null) {

                this.getStyleClass().remove("transparentCell");


                int key = item.getKey();
                Song song = item.getValue();
                int limit = 40;

                String tempName;
                if(song.getName().length() > limit) {
                    tempName = song.getName().substring(0, limit-1)+"...";
                } else {
                    tempName = song.getName();
                }

                String tempArtist;
                if(song.getArtist().length() > limit) {
                    tempArtist = song.getArtist().substring(0, limit-1)+"...";
                } else {
                    tempArtist = song.getArtist();
                }


                remove.setOnAction(handler -> {
                    TabViewController.RemoveEvent event = new TabViewController.RemoveEvent(eventRemove, key);
                    remove.fireEvent(event);
                });


                songInfo.setText(tempName + "\n"+tempArtist);
                box.getChildren().addAll(songInfo, remove);
               setGraphic(box);

            }

        }

    }


}
