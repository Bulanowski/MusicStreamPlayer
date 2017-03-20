package model;

import java.util.EventListener;

public interface AudioPlayingListener extends EventListener{
	public void AudioOn(AudioPlayingEvent ev);
}
