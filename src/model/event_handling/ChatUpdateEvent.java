package model.event_handling;

import java.util.EventObject;

public class ChatUpdateEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3427187603701983501L;
	private String msg;
	
	
	public ChatUpdateEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}
	
	public ChatUpdateEvent(Object source, String msg) {
		super(source);
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

}
