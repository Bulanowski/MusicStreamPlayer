package view;

import java.util.EventListener;

public interface VolumeListener extends EventListener {
	public void volumeChanged(VolumeEvent ev);

}
