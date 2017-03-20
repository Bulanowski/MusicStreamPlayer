package model.event_handling;

import java.util.EventObject;

public class VolumeEvent extends EventObject {

	private static final long serialVersionUID = -8631000583045187600L;
	private float volumeChange;

	public VolumeEvent(Object source) {
		super(source);
	}

	public VolumeEvent(Object source, float volumeChange) {
		super(source);
		this.volumeChange = volumeChange;
	}

	public float getVolumeChange() {
		return volumeChange;
	}

}
