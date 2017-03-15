package view;

import java.util.EventObject;

public class VolumeEvent extends EventObject {
	
	private float volumeChange;
	
	public VolumeEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}
	
	public VolumeEvent(Object source, float volumeChange) {
		super(source);
		this.volumeChange = volumeChange;
	}
	
	
	public float getVolumeChange() {
		return volumeChange;
	}

}
