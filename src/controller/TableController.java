package controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

class TableController {
    private ObservableList<Song> songList;
    private final TableView<Song> table;
    private final PrimaryView primaryView;

    TableController(PrimaryView primaryView, CommandSender commandCtrl) {
        this.primaryView = primaryView;
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No media"));
        TableColumn<Song, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Song, String> artist = new TableColumn<>("Artist");
        artist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        TableColumn<Song, String> album = new TableColumn<>("Album");
        album.setCellValueFactory(new PropertyValueFactory<>("album"));
        TableColumn<Song, String> length = new TableColumn<>("Length");
        length.setCellValueFactory(new PropertyValueFactory<>("trackLengthFormatted"));

        //noinspection unchecked
        table.getColumns().addAll(name, artist, album,length);

        table.setRowFactory(tableView -> {
            final TableRow<Song> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem addToQueueItem = new MenuItem("Add To Queue");
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    commandCtrl.addToQueue(row.getItem().getID());
                }
            });
            addToQueueItem.setOnAction(event -> commandCtrl.addToQueue(row.getItem().getID()));
            rowMenu.getItems().add(addToQueueItem);

            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu)
                    .otherwise((ContextMenu) null));
            return row;
        });

    }

    public void setAsCenter() {
        primaryView.setCenter(table);
    }

    public void addSongModelListChangeListener(SongModel songModel) {
        songModel.getSongs().addListener((ListChangeListener<Song>) change -> {
            songList = songModel.getSongs();
            table.setItems(songList);
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
