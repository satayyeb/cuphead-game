package cuphead.gfx.view;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.Serializable;

public class Music  implements Serializable {
    private AudioClip audioClip;

    public Music(String path, boolean repeat) {
        try {
            audioClip = new AudioClip(new File(path).toURI().toString());
            if (repeat)
                audioClip.setCycleCount(AudioClip.INDEFINITE);
            audioClip.play();
        } catch (Exception e) {
            System.out.println("cannot load music / " + e.getMessage());
        }
    }

    public void play() {
        audioClip.play();
    }

    public void stop() {
        audioClip.stop();
    }

    public void setVolume(double volume) {
        audioClip.stop();
        audioClip.play(volume);
    }
}
