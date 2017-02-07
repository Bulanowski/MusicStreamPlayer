package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import musicLibrary.Song;

public class MainStage extends Application {
	static TreeItem<String> root;
	static ObservableList<Song> songs;
	static HBox statusBar; 
	static Text numberOfItems;
	static TableColumn<Song,String> name;
	static TableColumn<Song,String> artist;
	static TableColumn<Song,String> album;
	
	public static void main(String[] args) {
		launch();
	}
	
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		songs = FXCollections.observableArrayList();
		BorderPane bp = new BorderPane();
		root = new TreeItem<String>("Artists");
		root.setExpanded(true);
		TreeView<String> tv = new TreeView<>(root);
		tv.prefWidthProperty().bind(bp.widthProperty().divide(5));
		TableView<Song> table = new TableView<>();
		table.setPlaceholder(new Label("Ain't no shit in this bitch"));
		name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<Song,String>("name"));
		artist = new TableColumn<>("Artist");
		artist.setCellValueFactory(new PropertyValueFactory<Song,String>("artist"));
		album = new TableColumn<>("Album");
		album.setCellValueFactory(new PropertyValueFactory<Song,String>("album"));
		
		name.prefWidthProperty().bind(table.widthProperty().divide(3));
		artist.prefWidthProperty().bind(table.widthProperty().divide(3));
		album.prefWidthProperty().bind(table.widthProperty().divide(3));
		
		table.setItems(songs);
		table.getColumns().addAll(name,artist, album);
		statusBar = new HBox(10);
		numberOfItems = new Text(songs.size()+" songs");
		statusBar.getChildren().add(numberOfItems);
		statusBar.setAlignment(Pos.CENTER);
		statusBar.setPadding(new Insets(5,5,5,5));
		bp.setLeft(tv);
		bp.setCenter(table);
		bp.setTop(TheMenuBar.get());
		bp.setBottom(statusBar);
		primaryStage.setScene(new Scene(bp,1000,800));
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(400);
		primaryStage.show();
	}
	
	public static void addToTree(TreeItem<String> m) {
		root.getChildren().add(m);
	}
	
	public static void addToTable(Song s) {
		statusBar.getChildren().clear();
		numberOfItems = new Text(songs.size()+" songs");
		statusBar.getChildren().add(numberOfItems);
		
		
		songs.add(s);
	}


	
	
	
	
	
}
