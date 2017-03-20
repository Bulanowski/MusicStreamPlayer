package view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class ConnectDialog extends Dialog<Pair<String,String>> {

	public ConnectDialog() {
		super();
		ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
//		this.getDialogPane().setHeaderText("Enter the IP you wish to connect to.");
//		this.getDialogPane().setContentText("IP:");
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField ipAddress = new TextField();
		ipAddress.setPromptText("IP Address");
		TextField username = new TextField();
		username.setPromptText("Username");

		grid.add(new Label("IP Address:"), 0, 0);
		grid.add(ipAddress, 1, 0);
		grid.add(new Label("Username:"), 0, 1);
		grid.add(username, 1, 1);
		
		
		this.getDialogPane().getButtonTypes().add(connectButtonType);
		
		
		Node connectButton = this.getDialogPane().lookupButton(connectButtonType);
		connectButton.setDisable(true);
		
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    connectButton.setDisable(newValue.trim().isEmpty());
		});
		
		this.setResultConverter(dialogButton -> {
			if(dialogButton  == connectButtonType) {
				return new Pair<>(ipAddress.getText(), username.getText());
			} else {
				return null;
			}
		});
		
		
		this.getDialogPane().setContent(grid);
		
	}

}
