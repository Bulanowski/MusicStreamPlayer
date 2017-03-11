package controller;

import model.TCP;
import view.PrimaryView;

public class MainController {
	private PrimaryView pv;

	public MainController(PrimaryView primaryView) {
		this.pv = primaryView;

		TCP tcp = new TCP();

		TableController tableCtrl = new TableController(primaryView, tcp);
		TreeController treeCtrl = new TreeController(primaryView, tableCtrl);
		StatusController statusCtrl = new StatusController(primaryView);
		ChatController chatBoxCtrl = new ChatController(primaryView, tcp);
		MenuController menuCtrl = new MenuController(primaryView, treeCtrl, tableCtrl, statusCtrl, chatBoxCtrl, tcp);
	}

}