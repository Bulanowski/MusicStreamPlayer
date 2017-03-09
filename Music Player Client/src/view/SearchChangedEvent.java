package view;

import java.util.EventObject;

public class SearchChangedEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5381240722915805664L;
	private String searchText;
	
	public SearchChangedEvent(Object source) {
		super(source);
	}
	
	public SearchChangedEvent(Object source, String searchText) {
		super(source);
		this.searchText = searchText;
	}
	
	public String getSearchText() {
		return searchText;
	}

}
