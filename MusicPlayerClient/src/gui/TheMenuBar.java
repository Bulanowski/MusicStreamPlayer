package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tcp.TCPClient;

public class TheMenuBar {
	public static MenuBar get() {
		MenuBar mb = new MenuBar();
		
		Label menuLabel = new Label("Connect");
	    menuLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
			
				Stage stage = new Stage();
				Label ipLabel = new Label("IP:");
				TextField ipField = new TextField();
				HBox hb = new HBox();
				hb.setAlignment(Pos.CENTER);
				hb.getChildren().addAll(ipLabel,ipField);
				VBox vb = new VBox(10);
				vb.setPadding(new Insets(10,0,0,0));
				Button connectButton = new Button("Connect");
				
				connectButton.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						try {
							TCPClient.connect(ipField.getText());
							stage.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				vb.getChildren().addAll(hb,connectButton);
				stage.setScene(new Scene(vb, 200,100));
				stage.showAndWait();
			}
		});
		Menu connect = new Menu();
		Menu search = new Menu();
		TextField searchField = new TextField();
		connect.setGraphic(menuLabel);
		search.setGraphic(searchField);
		mb.getMenus().addAll(connect,search);
		return mb;
	}
}
