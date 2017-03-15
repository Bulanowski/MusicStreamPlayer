package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class TCPSongDAO implements SongDAO {

	private TCP tcp;

	public TCPSongDAO(TCP tcp, String ipAddress) {
		this.tcp = tcp;
		this.tcp.connect(ipAddress, 6789);
	}

	@Override
	public Song get(String songPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Song> getAll() {
		List<Song> songs = new ArrayList<Song>();
		try {
			if (tcp.isConnected()) {
				tcp.sendCommand("request_songs");
				ObjectInputStream ois = new ObjectInputStream(tcp.getInputStream());
				Object readObject = ois.readObject();
				if (readObject instanceof List) {
					songs = (List<Song>) readObject;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return songs;
	}

	@Override
	public boolean update(String songPath, Song song) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String songPath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean create(Song song) {
		// TODO Auto-generated method stub
		return false;
	}

}
