package model;

import java.util.EventListener;

public interface SongListUpdateListener extends EventListener {
	public void songListUpdate(SongListUpdateEvent ev);
}
