package model;

import java.util.EventObject;

public class SearchChangedEvent extends EventObject {

	private static final long serialVersionUID = -4401570354274203433L;
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
