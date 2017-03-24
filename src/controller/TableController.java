package controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import model.CommandSender;
import model.Song;
import model.SongModel;
import model.TableFilter;
import view.PrimaryView;

public class TableController {
	private ObservableList<Song> songList;
	private TableView<Song> table;
	private TableColumn<Song, String> name;
	private TableColumn<Song, String> artist;
	private TableColumn<Song, String> album;

	@SuppressWarnings("unchecked")
	public TableController(PrimaryView primaryView, CommandSender commandCtrl) {
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
				row.setOnMouseClicked( event -> {
					if(event.getClickCount() == 2 && ! row.isEmpty()) {
						commandCtrl.addToQueue(row.getItem().getID());
					}
				});
				addToQueueItem.setOnAction(event -> {
						commandCtrl.addToQueue(row.getItem().getID()); 
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

	public void addSongModelListChangeListener(SongModel songModel) {
		songModel.getSongs().addListener(new ListChangeListener<Song>() {

			@Override
			public void onChanged(Change<? extends Song> change) {
				songList = songModel.getSongs();
				table.setItems(songList);
			}

		});
	}

	public void applyFilter(String filter, TableFilter filterType) {
		if (songList != null && !songList.isEmpty()) {
			ObservableList<Song> filteredList = FXCollections.observableArrayList();
			switch (filterType) {
			case NONE:
				filteredList = songList;
				break;
			case ARTISTS:
				for (Song song : songList)
					if (song.getArtist().equalsIgnoreCase(filter))
						filteredList.add(song);
				break;
			case ALBUMS:
				for (Song song : songList)
					if (song.getAlbum().equalsIgnoreCase(filter))
						filteredList.add(song);
				break;
			case SONGS:
				for (Song song : songList)
					if (song.getName().equalsIgnoreCase(filter))
						filteredList.add(song);
				break;
			case ALL:
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
