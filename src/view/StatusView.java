package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.event_handling.VolumeEvent;
import model.event_handling.VolumeListener;

public class StatusView {
	private VBox statusBar;
	private Text numberOfItems;
	private Slider volumeSlider;
	private VolumeListener volumeListener;

	public StatusView() {
		statusBar = new VBox(10);
		volumeSlider = new Slider();
		statusBar.setAlignment(Pos.CENTER);
		statusBar.setPadding(new Insets(5, 5, 5, 5));
		volumeSlider.setMin(-60);
		volumeSlider.setMax(6);
		volumeSlider.setMaxWidth(200);
		volumeSlider.setValue(0);
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				VolumeEvent ev = new VolumeEvent(this, new_val.floatValue());
				if (volumeListener != null) {
					volumeListener.volumeChanged(ev);

				}
			}
		});

	}

	public void setSongCount(int size) {
		numberOfItems = new Text(size + " songs");
		resetStatusBar();
	}

	public void setSongInfo(String artistName, String songName) {
		statusBar.getChildren().clear();
		statusBar.getChildren().addAll(new Label(artistName), new Label(songName), volumeSlider);
	}

	public void resetStatusBar() {
		statusBar.getChildren().clear();
		if (numberOfItems != null) {
			statusBar.getChildren().addAll(numberOfItems, volumeSlider);
		}
	}

	public void setVolumeListener(VolumeListener volumeListener) {
		this.volumeListener = volumeListener;
	}

	public Node getNode() {
		return statusBar;
	}
}
