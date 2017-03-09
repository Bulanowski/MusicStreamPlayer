package controller;

import java.util.HashMap;
import java.util.TreeSet;

import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Song;
import model.SongModel;
import view.MediaTreeView;
import view.PrimaryView;

public class TreeController {
	private HashMap<String, TreeSet<String>> mediaList;
	private MediaTreeView mtv;

	public TreeController(PrimaryView primaryView, TableController tblCtrl) {
		mtv = new MediaTreeView();

		mtv.getTreeView().setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
			@Override
			public TreeCell<String> call(TreeView<String> treeView) {
				return new CustomTreeCell(tblCtrl);
			}
		});

		primaryView.setLeft(mtv.getTreeView());
		mtv.getTreeView().prefWidthProperty().bind(primaryView.getLeftWidth());
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

	private final class CustomTreeCell extends TreeCell<String> {
		
		TableController tblCtrl;
		
		CustomTreeCell(TableController tblCtrl) {
			this.tblCtrl = tblCtrl;
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				setText(item.toString());
				setGraphic(getTreeItem().getGraphic());
			}
			

			TreeItem<String> selected = super.getTreeItem();
			setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (selected != null) {
						if (selected.getValue().equals("Artists")) {
							tblCtrl.applyFilter("", 0);
						} else if (selected.getParent().getValue().equals("Artists")) {
							tblCtrl.applyFilter(selected.getValue(), 1);
						} else {
							tblCtrl.applyFilter(selected.getValue(), 2);
						}
					}
				}
			});
			
		}
	}

}
