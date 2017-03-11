package controller;

import view.PrimaryView;

public class MainController {
	private PrimaryView pv;

	public MainController(PrimaryView primaryView) {
		this.pv = primaryView;

		TCPController tcpCtrl = new TCPController();

		TableController tableCtrl = new TableController(primaryView, tcpCtrl);
		TreeController treeCtrl = new TreeController(primaryView, tableCtrl);
		StatusController statusCtrl = new StatusController(primaryView);
		ChatController chatBoxCtrl = new ChatController(primaryView, tcpCtrl);
		MenuController menuCtrl = new MenuController(primaryView, tcpCtrl, treeCtrl, tableCtrl, statusCtrl, chatBoxCtrl);
	}

}