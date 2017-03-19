package controller;

import model.TCP;

public class CommandController {
	
	private TCP tcp;
	
	public CommandController(TCP tcp) {
		this.tcp = tcp;
	}
	
	public void requestSongs() {
		tcp.sendCommand("request_songs");
	}
	
	public void addToQueue(String songPath) {
		tcp.sendCommand("add_to_queue " + songPath);
	}
	
	public void chat(String chatMessage) {
		tcp.sendCommand("chat " + chatMessage);
	}

}
