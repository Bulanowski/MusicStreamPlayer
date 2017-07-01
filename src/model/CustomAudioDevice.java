package model;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.JavaSoundAudioDevice;

import javax.sound.sampled.*;
import java.lang.reflect.Field;

/**
 * Created by Alex on 6/29/2017.
 */
public class CustomAudioDevice extends JavaSoundAudioDevice {
    private FloatControl volume;
    private final int bufferSize = 2500;

    protected void createSource() throws JavaLayerException {
        try {
            Line line = AudioSystem.getLine(this.getSourceLineInfo());
            if(line instanceof SourceDataLine) {
                Field sourceField = this.getClass().getSuperclass().getDeclaredField("source");
                Field formatField = this.getClass().getSuperclass().getDeclaredField("fmt");

                sourceField.setAccessible(true);
                formatField.setAccessible(true);

                sourceField.set(this, line);
                SourceDataLine sourceDataLine = (SourceDataLine) sourceField.get(this);
                sourceDataLine.open((AudioFormat) formatField.get(this), bufferSize);
                volume = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                sourceDataLine.start();

                formatField.setAccessible(false);
                sourceField.setAccessible(false);
            }
        } catch (RuntimeException | LinkageError | NoSuchFieldException | LineUnavailableException | IllegalAccessException ex) {
            throw new JavaLayerException("cannot obtain source audio line", ex);
        }
    }


    public float getVolume() {
        return volume.getValue();
    }

    public void setVolume(float newVolume) {
        if (volume != null) {
            volume.setValue(newVolume);
        }
    }
}
