package model.event_handling;

import java.util.EventListener;

public interface AudioPlayingListener extends EventListener{
	public void AudioOn(AudioPlayingEvent ev);
}
