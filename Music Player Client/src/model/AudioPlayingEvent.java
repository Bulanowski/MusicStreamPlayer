package model;

import java.util.EventObject;

public class AudioPlayingEvent extends EventObject {

	VolumeListener volumeListener;
	
	public AudioPlayingEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}
	
	
	public AudioPlayingEvent(Object source, VolumeListener volumeListener) {
		super(source);
		this.volumeListener = volumeListener;
		// TODO Auto-generated constructor stub
	}
	
	public VolumeListener getVolumeListener() {
		return volumeListener;
	}

}
