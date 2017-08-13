package view;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.concurrent.atomic.AtomicLong;

public class StatusView {
	private final VBox statusBar;
	private Text numberOfItems;
	private  Text songInfo;
	private Text currentPosition;
	private Text trackLengthText;
	private final Slider volumeSlider;
	private final ProgressBar progressBar;
	private final Button skip;
	private HBox progress;

	public StatusView() {
		statusBar = new VBox(10);
		volumeSlider = new Slider();
		skip = new Button(">");
		progressBar = new ProgressBar();
		currentPosition = new Text();
		trackLengthText = new Text();
		progress = new HBox(10);
        songInfo = new Text();
		progressBar.setMaxHeight(5);
		statusBar.setAlignment(Pos.CENTER);
		statusBar.setPadding(new Insets(5, 5, 5, 5));
		volumeSlider.setMin(-60);
		volumeSlider.setMax(6);
		volumeSlider.setMaxWidth(200);
		volumeSlider.setValue(0);
	}

	public void setSkipListener(ChangeListener<Boolean> listener) {
	     skip.armedProperty().addListener(listener);
    }

	public void setSongCount(int size) {
		numberOfItems = new Text(size + " songs");
		resetStatusBar();
	}

	public void updateStatusBar() {
        statusBar.getChildren().clear();
        progress.getChildren().clear();
        progress.setAlignment(Pos.CENTER);
        progress.getChildren().addAll(currentPosition,progressBar,trackLengthText);
        statusBar.getChildren().addAll(songInfo, progress,volumeSlider,skip);
    }

	public void setTrackLength(long currentTime, int trackLength) {
        if(currentTime != -1) {
            double num = (double)currentTime/(double)trackLength;
            currentPosition.setText(currentTime/60 +":"+((currentTime%60 < 10)? "0"+currentTime%60:currentTime%60));
            trackLengthText.setText(trackLength/60 +":"+((trackLength%60 < 10)? "0"+trackLength%60:trackLength%60));
            progressBar.setProgress(num);
//            updateStatusBar();
        }
    }

	public void setSongInfo(String artistName, String songName) {
		if(!artistName.isEmpty() && !songName.isEmpty()) {
		    songInfo.setText(artistName+" - "+songName.trim());
			updateStatusBar();
		} else {
//			resetStatusBar();
		}

	}

    public void resetStatusBar() {
		statusBar.getChildren().clear();
		if (numberOfItems != null) {
			statusBar.getChildren().addAll(numberOfItems, volumeSlider);
		}
	}

	public void addVolumeListener(ChangeListener<Number> listener) {
		volumeSlider.valueProperty().addListener(listener);
	}

	public Node getNode() {
		return statusBar;
	}
}
