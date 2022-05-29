package cuphead.gfx.view;

import cuphead.gfx.Main;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.Serializable;

public class Music  implements Serializable {
    private AudioClip audioClip;

    public Music(String path, boolean repeat) {
        try {
            String x = Main.getResource(path);
            audioClip = new AudioClip(x);
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
