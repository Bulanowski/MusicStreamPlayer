package controller;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import model.CommandSender;
import model.Song;
import model.SongModel;
import view.PrimaryView;
import view.StatusView;

class StatusController {
	private final StatusView sv;
	private final PrimaryView primaryView;
	private Pair<SimpleStringProperty,SimpleStringProperty> songInfo;
	private boolean queueEmpty = false;
	public StatusController(PrimaryView primaryView, CommandSender commandSender) {
	    this.primaryView = primaryView;
		sv = new StatusView();

		sv.setSkipListener((one,two,three)-> {
		    if(two) {
                commandSender.voteToSkip();
            }
        });


	}

	public void setAsBottom() {
	    primaryView.setBottom(sv.getNode());
    }
	
	public void addSongModelListChangeListener(SongModel songModel) {
		songModel.getSongs().addListener((ListChangeListener<Song>) change -> Platform.runLater(() -> sv.setSongCount(songModel.getSongs().size())));
	}

	public void addSongInfoChangeListener(Pair<SimpleStringProperty,SimpleStringProperty> info) {
		info.getValue().addListener((one, two, three) -> Platform.runLater(() -> {
		    songInfo = info;

            songInfo.getKey().addListener((one1, two1, three1) -> {
                if(songInfo.getKey().get().equals("") && queueEmpty) {
                    Platform.runLater(() -> sv.resetStatusBar() );
                }
            });

                sv.setSongInfo(info.getKey().getValue(),info.getValue().getValue());}));
	}

    public void addTrackPosition(Pair<SimpleIntegerProperty,SimpleIntegerProperty> info) {
        info.getKey().addListener((one, two, three) -> Platform.runLater(() -> sv.setTrackLength(info.getKey().get(),info.getValue().get())));
    }
	
	public void addVolumeListener(ChangeListener<Number> listener) {
		sv.addVolumeListener(listener);
	}

	public ListChangeListener getListListener() {
	    ListChangeListener listener = change -> {
            if(change.getList().isEmpty()) {
                System.out.println("QUEUE IS EMPTY");
                queueEmpty = true;
            }
//            if(change.getList().isEmpty() && songInfo.getKey().get().equals("")) {
//                Platform.runLater(() -> sv.resetStatusBar() );
//            }
        };
	    return listener;
    }
}
