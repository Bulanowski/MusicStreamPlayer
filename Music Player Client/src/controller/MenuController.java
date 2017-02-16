package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.MenuView;
import view.PrimaryView;

public class MenuController {
	private MenuView menuView;

	public MenuController(PrimaryView primaryView, TCPController tcpCtrl, TreeController treeCtrl,
			TableController tableCtrl, StatusController statusCtrl) {

		menuView = new MenuView();

		menuView.onClickEvent(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				Stage stage = new Stage();
				Label ipLabel = new Label("IP: ");
				TextField ipField = new TextField();
				HBox hb = new HBox();
				hb.setAlignment(Pos.CENTER);
				hb.getChildren().addAll(ipLabel, ipField);
				VBox vb = new VBox(10);
				vb.setPadding(new Insets(10, 0, 0, 0));
				Button connectButton = new Button("Connect");

				connectButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						try {
							tcpCtrl.connect(ipField.getText());
							stage.close();
							Thread.sleep(1000);
							treeCtrl.updateArtists(tcpCtrl.getArtistModel());
							tableCtrl.updateSongs(tcpCtrl.getSongModel());
							statusCtrl.updateCount(tcpCtrl.getSongModel());

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				vb.getChildren().addAll(hb, connectButton);
				stage.setScene(new Scene(vb, 200, 100));
				stage.showAndWait();
			};
		});

		primaryView.setTop(menuView.getNode());
	}
}
