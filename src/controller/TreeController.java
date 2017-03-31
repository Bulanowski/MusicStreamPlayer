package controller;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import model.Song;
import model.SongModel;
import model.TableFilter;
import view.PrimaryView;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

class TreeController {
	private final TreeItem<String> rootNode;

	TreeController(PrimaryView primaryView, TableController tblCtrl) {
		rootNode = new TreeItem<>("Artists");
		rootNode.setExpanded(true);

		TreeView<String> tree = new TreeView<>(rootNode);
		tree.setCellFactory(treeView -> new CustomTreeCell(tblCtrl));
		primaryView.setLeft(tree);
		tree.prefWidthProperty().bind(primaryView.getLeftWidth());
	}

	void clear() {
		rootNode.getChildren().clear();
	}
	
	void addSongModelListChangeListener(SongModel songModel) {
		songModel.getSongs().addListener(new ListChangeListener<Song>(){

			@Override
			public synchronized void onChanged(Change<? extends Song> change) {
				HashMap<String, TreeSet<String>> mediaList = new HashMap<>();
				for (Song song : songModel.getSongs()) {
					if (!mediaList.containsKey(song.getArtist())) {
						mediaList.put(song.getArtist(), new TreeSet<>());
					}
					mediaList.get(song.getArtist()).add(song.getAlbum());
				}
				for (Entry<String, TreeSet<String>> artist : mediaList.entrySet()) {
					TreeItem<String> artistItem = new TreeItem<>(artist.getKey());
					for (String album : artist.getValue()) {
						TreeItem<String> albumItem = new TreeItem<>(album);
						artistItem.getChildren().add(albumItem);
					}
					rootNode.getChildren().add(artistItem);
				}
			}
			
		});
	}

	private final class CustomTreeCell extends TreeCell<String> {
		
		final TableController tblCtrl;
		
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
				setText(item);
				setGraphic(getTreeItem().getGraphic());
			}
			

			TreeItem<String> selected = super.getTreeItem();
			setOnMouseClicked(event -> {
                if (selected != null) {
                    if (selected.getValue().equals("Artists")) {
                        tblCtrl.applyFilter(null, TableFilter.NONE);
                    } else if (selected.getParent().getValue().equals("Artists")) {
                        tblCtrl.applyFilter(selected.getValue(), TableFilter.ARTISTS);
                    } else {
                        tblCtrl.applyFilter(selected.getValue(), TableFilter.ALBUMS);
                    }
                }
            });

		}
	}

}
