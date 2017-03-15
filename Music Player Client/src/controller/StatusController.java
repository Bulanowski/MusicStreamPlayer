package controller;

import model.SongModel;
import view.PrimaryView;
import view.StatusView;
import view.VolumeListener;

public class StatusController {
	private StatusView sv;

	public StatusController(PrimaryView primaryView) {
		sv = new StatusView();

		primaryView.setBottom(sv.getNode());
	}

	public void updateCount(SongModel songModel) {
		sv.setSongCount(songModel.getSongs().size());
	}
	
	
	public void setVolumeListener(VolumeListener volumeListener) {
		sv.setVolumeListener(volumeListener);
	}
}
