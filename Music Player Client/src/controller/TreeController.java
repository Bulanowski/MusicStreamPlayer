package controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import model.Album;
import model.Artist;
import model.ArtistModel;
import view.PrimaryView;

public class TreeController {
	private PrimaryView pv;
	private TreeItem<String> rootNode;

	public TreeController(PrimaryView primaryView) {
		this.pv = primaryView;
		rootNode = new TreeItem<String>("Artists");
		rootNode.setExpanded(true);

		TreeView<String> tv = new TreeView<>(rootNode);

		pv.setLeft(tv);

		tv.prefWidthProperty().bind(pv.getLeftWidth());
	}

	public void updateArtists(ArtistModel artistModel) {
		for (Artist artist : artistModel.getArtists()) {
			TreeItem<String> artistItem = new TreeItem<String>(artist.getName());
			for (Album album : artist.getAlbumList()) {
				TreeItem<String> albumItem = new TreeItem<String>(album.getName());
				artistItem.getChildren().add(albumItem);
			}
			rootNode.getChildren().add(artistItem);
		}
	}

}
