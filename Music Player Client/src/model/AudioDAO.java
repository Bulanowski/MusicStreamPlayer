package model;

public interface AudioDAO {
	
	public void reset();
	
	public byte[] getAudioBuffer();
	
	public boolean getPlaying();
	
	public void startPlaying();
	
	public void stopPlaying();

}
