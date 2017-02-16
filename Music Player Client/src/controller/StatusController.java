package controller;

import model.SongModel;
import view.PrimaryView;
import view.StatusView;

public class StatusController {
	private StatusView sv;

	public StatusController(PrimaryView primaryView) {
		sv = new StatusView();

		primaryView.setBottom(sv.getNode());
	}

	public void updateCount(SongModel songModel) {
		sv.setSongCount(songModel.getSongs().size());
	}
}
