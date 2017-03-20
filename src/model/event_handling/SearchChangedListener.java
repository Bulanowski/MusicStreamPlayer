package model.event_handling;

import java.util.EventListener;

public interface SearchChangedListener extends EventListener {

	public void searchChanged(SearchChangedEvent event);

}
