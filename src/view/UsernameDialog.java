package view;

import javafx.scene.control.TextInputDialog;

public class UsernameDialog extends TextInputDialog {

	public UsernameDialog() {
		super();
		this.getDialogPane().setHeaderText("Enter username.");
	}

}
