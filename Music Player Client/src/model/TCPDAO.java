package model;

import javafx.collections.ObservableList;

public class TCPDAO implements DAO {
	
	TCP tcp;
	
	public TCPDAO () {
		tcp = new TCP();
	}

	@Override
	public ObservableList<Song> getSongs() {
		return null;
	}

}
