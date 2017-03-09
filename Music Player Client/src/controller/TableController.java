package controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Song;
import model.SongModel;
import view.PrimaryView;

public class TableController {
	private ObservableList<Song> songList;
	private TableView<Song> table;
	private TableColumn<Song, String> name;
	private TableColumn<Song, String> artist;
	private TableColumn<Song, String> album;

	public TableController(PrimaryView primaryView, TCPController tcpCtrl) {
		table = new TableView<>();
		table.setPlaceholder(new Label("No media"));
		name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));
		artist = new TableColumn<>("Artist");
		artist.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
		album = new TableColumn<>("Album");
		album.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));

		table.getColumns().addAll(name, artist, album);

		table.setRowFactory(new Callback<TableView<Song>, TableRow<Song>>() {

			@Override
			public TableRow<Song> call(TableView<Song> tableView) {
				final TableRow<Song> row = new TableRow<>();
				final ContextMenu rowMenu = new ContextMenu();
				MenuItem addToQueueItem = new MenuItem("Add To Queue");
				addToQueueItem.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						tcpCtrl.sendCommand("add_to_queue " + row.getItem().getPath());
					}

				});
				rowMenu.getItems().add(addToQueueItem);

				row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu)
						.otherwise((ContextMenu) null));
				return row;
			}

		});

		name.prefWidthProperty().bind(table.widthProperty().divide(3));
		artist.prefWidthProperty().bind(table.widthProperty().divide(3));
		album.prefWidthProperty().bind(table.widthProperty().divide(3));

		primaryView.setCenter(table);
	}

	public void updateSongs(SongModel songModel) {
		songList = songModel.getSongs();
		table.setItems(songList);
	}

	// type: 0 = none, 1 = artist, 2 = album, 3 = song, 4 = all
	public void applyFilter(String filter, int type) {
		if (songList != null && !songList.isEmpty()) {
			ObservableList<Song> filteredList = FXCollections.observableArrayList();
			switch (type) {
			case 0:
				filteredList = songList;
				break;
			case 1:
				for (Song song : songList)
					if (song.getArtist().toLowerCase().equals(filter.toLowerCase()))
						filteredList.add(song);
				break;
			case 2:
				for (Song song : songList)
					if (song.getAlbum().toLowerCase().equals(filter.toLowerCase()))
						filteredList.add(song);
				break;
			case 3:
				for (Song song : songList)
					if (song.getName().toLowerCase().contains(filter.toLowerCase()))
						filteredList.add(song);
				break;
			case 4:
				for (Song song : songList)
					if (song.getArtist().toLowerCase().contains(filter.toLowerCase())
							|| song.getAlbum().toLowerCase().contains(filter.toLowerCase())
							|| song.getName().toLowerCase().contains(filter.toLowerCase()))
						filteredList.add(song);
				break;
			}
			table.setItems(filteredList);
		}
	}
}
