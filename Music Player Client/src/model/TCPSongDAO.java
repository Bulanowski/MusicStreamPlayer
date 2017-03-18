package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class TCPSongDAO implements SongDAO {

	private TCP tcp;

	public TCPSongDAO(TCP tcp) {
		this.tcp = tcp;
//		tcp.addPackageReceivedListener(new PackageReceivedListener() {
//			
//			@Override
//			public void readPackage(PackageReceivedEvent ev) {
//				if (ev.getPackageType() == PackageType.SONG_LIST.getByte()) {
//					if (ev.getInformation() instanceof List) {
//						songs = (List<Song>) ev.getInformation();
//					}
//				}
//			}
//		});
	}

	@Override
	public Song get(String songPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Song> getAll() {
		List<Song> songs = new ArrayList<Song>();
		if (tcp.isConnected()) {
			Object readObject = tcp.sendCommandWithReturn("request_songs");
			if (readObject instanceof List) {
				songs = (List<Song>) readObject;
			}
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
