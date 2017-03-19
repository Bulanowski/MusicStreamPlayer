package controller;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Song;
import model.SongModel;
import model.TableFilter;
import view.PrimaryView;

public class TreeController {
	private TreeView<String> tree;
	private TreeItem<String> rootNode;

	public TreeController(PrimaryView primaryView, TableController tblCtrl) {
		rootNode = new TreeItem<String>("Artists");
		rootNode.setExpanded(true);
		
		tree = new TreeView<String>(rootNode);
		tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
			@Override
			public TreeCell<String> call(TreeView<String> treeView) {
				return new CustomTreeCell(tblCtrl);
			}
		});
		primaryView.setLeft(tree);
		tree.prefWidthProperty().bind(primaryView.getLeftWidth());
	}
	
	public void clear() {
		rootNode.getChildren().clear();
	}
	
	public void addSongModelListChangeListener(SongModel songModel) {
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
					TreeItem<String> artistItem = new TreeItem<String>(artist.getKey());
					for (String album : artist.getValue()) {
						TreeItem<String> albumItem = new TreeItem<String>(album);
						artistItem.getChildren().add(albumItem);
					}
					rootNode.getChildren().add(artistItem);
				}
			}
			
		});
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
							tblCtrl.applyFilter(null, TableFilter.NONE);
						} else if (selected.getParent().getValue().equals("Artists")) {
							tblCtrl.applyFilter(selected.getValue(), TableFilter.ARTISTS);
						} else {
							tblCtrl.applyFilter(selected.getValue(), TableFilter.ALBUMS);
						}
					}
				}
			});

		}
	}

}
