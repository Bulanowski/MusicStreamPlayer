package view;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class MediaTreeView {
	private TreeView<String> tv;
	private TreeItem<String> rootNode;
	
	public MediaTreeView() {
		rootNode = new TreeItem<String>("Artists");
		rootNode.setExpanded(true);
		
		tv = new TreeView<String>(rootNode);
	}
	
	public void updateTreeView(HashMap<String, TreeSet<String>> mediaList) {
		for (Entry<String, TreeSet<String>> artist : mediaList.entrySet()) {
			TreeItem<String> artistItem = new TreeItem<String>(artist.getKey());
			for (String album : artist.getValue()) {
				TreeItem<String> albumItem = new TreeItem<String>(album);
				artistItem.getChildren().add(albumItem);
			}
			rootNode.getChildren().add(artistItem);
		}
	}
	
	public Node getNode() {
		return tv;
	}

}
