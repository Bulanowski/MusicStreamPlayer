package view;

import java.util.EventListener;

public interface SearchChangedListener extends EventListener {
	
	public void searchChanged(SearchChangedEvent ev);

}
