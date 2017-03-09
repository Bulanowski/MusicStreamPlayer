package controller;

import java.util.HashMap;
import java.util.TreeSet;

import javafx.scene.control.TreeView;
import model.Song;
import model.SongModel;
import view.MediaTreeView;
import view.PrimaryView;

public class TreeController {
	private HashMap<String, TreeSet<String>> mediaList;
	private MediaTreeView mtv;

	public TreeController(PrimaryView primaryView) {
		mtv = new MediaTreeView();

		primaryView.setLeft(mtv.getNode());
		((TreeView<String>) mtv.getNode()).prefWidthProperty().bind(primaryView.getLeftWidth());
	}

	public void updateArtists(SongModel songModel) {
		mediaList = new HashMap<>();
		for (Song song : songModel.getSongs()) {
			if (!mediaList.containsKey(song.getArtist())) {
				mediaList.put(song.getArtist(), new TreeSet<>());
			}
			mediaList.get(song.getArtist()).add(song.getAlbum());
		}
		mtv.updateTreeView(mediaList);
	}

}
