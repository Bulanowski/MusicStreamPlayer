package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.VolumeEvent;
import model.VolumeListener;

public class StatusView {
	private HBox statusBar;
	private Text numberOfItems;
	private Slider volume;
	private VolumeListener volumeListener;

	public StatusView() {
		statusBar = new HBox(10);
		volume = new Slider();
		statusBar.setAlignment(Pos.CENTER);
		statusBar.setPadding(new Insets(5, 5, 5, 5));
		volume.setMin(-60);
		volume.setMax(6);
		volume.setValue(0);
		
		volume.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                        VolumeEvent ev = new VolumeEvent(this, new_val.floatValue());
                        if(volumeListener != null) {
                        	
                        	volumeListener.volumeChanged(ev);
                        }
                }
            });
		
	}
	
	public void setVolumeListener(VolumeListener volumeListener) {
		this.volumeListener = volumeListener;
	}

	public void setSongCount(int size) {
		numberOfItems = new Text(size + " songs");
		statusBar.getChildren().clear();
		statusBar.getChildren().addAll(numberOfItems, volume);
	}

	public Node getNode() {
		return statusBar;
	}
}
