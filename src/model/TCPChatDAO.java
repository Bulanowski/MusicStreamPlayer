package model;

import model.event_handling.ChatUpdateEvent;
import model.event_handling.ChatUpdateListener;
import model.event_handling.PackageReceivedEvent;
import model.event_handling.PackageReceivedListener;

public class TCPChatDAO implements ChatDAO {
	private ChatUpdateListener chatUpdateListener;
	
	
	public TCPChatDAO(TCP tcp) {
		tcp.addPackageReceivedListener(new PackageReceivedListener() {
			
			@Override
			public void readPackage(PackageReceivedEvent ev) {
				if (ev.getPackageType() == PackageType.CHAT.getByte()) {
					if(ev.getInformation() instanceof String) {
						ChatUpdateEvent event = new ChatUpdateEvent(this, (String)ev.getInformation());
						if(chatUpdateListener != null) {
							chatUpdateListener.chatUpdated(event);
						}
					}
				}
				
			}
		});
	}
	
	
	@Override
	public void setChatUpdateListener(ChatUpdateListener chatUpateListener) {
		this.chatUpdateListener = chatUpateListener;
		
	}

}
