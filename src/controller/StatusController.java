package controller;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import model.Song;
import model.SongModel;
import view.PrimaryView;
import view.StatusView;

class StatusController {
	private final StatusView sv;

	public StatusController(PrimaryView primaryView) {
		sv = new StatusView();

		primaryView.setBottom(sv.getNode());
	}
	
	public void addSongModelListChangeListener(SongModel songModel) {
		songModel.getSongs().addListener((ListChangeListener<Song>) change -> Platform.runLater(() -> sv.setSongCount(songModel.getSongs().size())));
	}

	public void addSongInfoChangeListener(Pair<SimpleStringProperty,SimpleStringProperty> info) {
		info.getValue().addListener((one, two, three) -> Platform.runLater(() -> sv.setSongInfo(info.getKey().getValue(),info.getValue().getValue())));
	}

    public void addTrackPosition(Pair<SimpleIntegerProperty,SimpleIntegerProperty> info) {
        info.getKey().addListener((one, two, three) -> Platform.runLater(() -> sv.setTrackLength(info.getKey().get(),info.getValue().get())));
    }
	
	public void addVolumeListener(ChangeListener<Number> listener) {
		sv.addVolumeListener(listener);
	}

	public void setSkipListener(ChangeListener<Boolean> listener) {
	    sv.setSkipListener(listener);
    }

	public ListChangeListener getListListener() {
	    /*TODO: Fix resetting status bar, as of right now checking
	    if the queue list is empty is not a good way to see if its
	    actually empty because it is emptied every time the list is changed.
	     */
	    ListChangeListener listener = change -> {
            if(change.getList().isEmpty()) {
//                System.out.println("QUEUE IS EMPTY");
                // Commented out because it doesnt work
//                Platform.runLater(() -> sv.resetStatusBar() );
            }
        };
	    return listener;
    }
}
