package model.event_handling;

import java.util.EventObject;

public class AudioPlayingEvent extends EventObject {

	private static final long serialVersionUID = 8078675809569425651L;
	VolumeListener volumeListener;
	
	public AudioPlayingEvent(Object source) {
		super(source);
	}
	
	
	public AudioPlayingEvent(Object source, VolumeListener volumeListener) {
		super(source);
		this.volumeListener = volumeListener;
	}
	
	public VolumeListener getVolumeListener() {
		return volumeListener;
	}

}
