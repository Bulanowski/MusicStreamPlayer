package model;

import java.util.List;

public class TCPSongDAO implements SongDAO {

	private TCP tcp;
	private SongListUpdateListener songListUpdateListener;

	public TCPSongDAO(TCP tcp) {
		this.tcp = tcp;
		tcp.addPackageReceivedListener(new PackageReceivedListener() {

			@Override
			public void readPackage(PackageReceivedEvent ev) {
				if (ev.getPackageType() == PackageType.SONG_LIST.getByte()) {
					if (ev.getInformation() instanceof List) {
						@SuppressWarnings("unchecked")
						SongListUpdateEvent event = new SongListUpdateEvent(this, (List<Song>) ev.getInformation());
						if (songListUpdateListener != null) {
							songListUpdateListener.songListUpdate(event);
						}
					}
				}
			}
		});
	}

	public void setSongListUpdateListener(SongListUpdateListener songListUpdateListener) {
		this.songListUpdateListener = songListUpdateListener;
	}

	@Override
	public void requestSongs() {
		tcp.sendCommand("request_songs");
	}

	@Override
	public Song get(String songPath) {
		// TODO Auto-generated method stub
		return null;
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
