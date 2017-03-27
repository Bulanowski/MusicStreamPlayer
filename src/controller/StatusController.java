package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import model.Song;
import model.SongModel;
import view.PrimaryView;
import view.StatusView;

public class StatusController {
	private StatusView sv;

	public StatusController(PrimaryView primaryView) {
		sv = new StatusView();

		primaryView.setBottom(sv.getNode());
	}
	
	public void addSongModelListChangeListener(SongModel songModel) {
		songModel.getSongs().addListener(new ListChangeListener<Song>(){

			@Override
			public void onChanged(Change<? extends Song> change) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						sv.setSongCount(songModel.getSongs().size());
					}
				});
			}
			
		});
	}
	
	public void addVolumeListener(ChangeListener<Number> listener) {
		sv.addVolumeListener(listener);
	}
	
//	public void setVolumeListener(VolumeListener volumeListener) {
//		sv.setVolumeListener(volumeListener);
//	}
}
