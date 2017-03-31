package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
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
	
	public void addVolumeListener(ChangeListener<Number> listener) {
		sv.addVolumeListener(listener);
	}
}
