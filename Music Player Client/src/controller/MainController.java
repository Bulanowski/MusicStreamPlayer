package controller;

import view.PrimaryView;

public class MainController {
	private PrimaryView pv;

	public MainController(PrimaryView primaryView) {
		this.pv = primaryView;

		TCPController tcpCtrl = new TCPController();

		TreeController treeCtrl = new TreeController(primaryView);
		TableController tableCtrl = new TableController(primaryView, tcpCtrl);
		StatusController statusCtrl = new StatusController(primaryView);
		MenuController menuCtrl = new MenuController(primaryView, tcpCtrl, treeCtrl, tableCtrl, statusCtrl);
	}

}