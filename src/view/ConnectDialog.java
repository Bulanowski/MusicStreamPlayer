package view;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class ConnectDialog extends TextInputDialog {

	public ConnectDialog() {
		super();
		ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
		this.getDialogPane().setHeaderText("Enter the IP you wish to connect to.");
		this.getDialogPane().setContentText("IP:");
		this.getDialogPane().getButtonTypes().set(0, connectButtonType);
//        this.getDialogPane().getStylesheets().add("CSS/main.css");
	}
}
