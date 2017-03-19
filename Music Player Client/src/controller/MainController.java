package controller;

import model.TCP;
import view.PrimaryView;

public class MainController {
	private PrimaryView pv;
	private TCP tcp;
	private TableController tableCtrl;
	private TreeController treeCtrl;
	private StatusController statusCtrl;
	private ChatController chatBoxCtrl;
	private MenuController menuCtrl;

	public MainController(PrimaryView primaryView) {
		this.pv = primaryView;

		tcp = new TCP();

		tableCtrl = new TableController(primaryView, tcp);
		treeCtrl = new TreeController(primaryView, tableCtrl);
		statusCtrl = new StatusController(primaryView);
		chatBoxCtrl = new ChatController(primaryView, tcp);
		menuCtrl = new MenuController(primaryView, treeCtrl, tableCtrl, statusCtrl, chatBoxCtrl, tcp);
	}

	public void onApplicationClosed() {
		tcp.disconnect();
		menuCtrl.onApplicationClosed();
	}

}