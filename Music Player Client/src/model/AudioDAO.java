package model;

public interface AudioDAO {
	
	public byte[] getAudioBuffer();
	
	public boolean getPlaying();
	
	public void startPlaying();
	
	public void stopPlaying();

}
