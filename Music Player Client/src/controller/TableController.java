package controller;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Song;
import model.SongModel;
import view.PrimaryView;

public class TableController {
	private TableView<Song> table;
	private TableColumn<Song, String> name;
	private TableColumn<Song, String> artist;
	private TableColumn<Song, String> album;

	public TableController(PrimaryView primaryView) {
		table = new TableView<>();
		table.setPlaceholder(new Label("Ain't no shit in this bitch"));
		name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));
		artist = new TableColumn<>("Artist");
		artist.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
		album = new TableColumn<>("Album");
		album.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));

		table.getColumns().addAll(name, artist, album);

		name.prefWidthProperty().bind(table.widthProperty().divide(3));
		artist.prefWidthProperty().bind(table.widthProperty().divide(3));
		album.prefWidthProperty().bind(table.widthProperty().divide(3));

		primaryView.setCenter(table);
	}

	public void updateSongs(SongModel songModel) {
		table.setItems(songModel.getSongs());
	}
}
