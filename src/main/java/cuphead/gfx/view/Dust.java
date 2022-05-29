package cuphead.gfx.view;

import cuphead.gfx.controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dust  implements Serializable {
    private final ImageView dust = new ImageView();
    private final List<Image> frames = new ArrayList<>();
    private int frameIndex = 0;

    public Dust(final Pane root, double x, double y, boolean isBird) {
        try {
            String path = isBird ? "./media/miniBossDeath/" : "./media/dust/";
            for (File file : Controller.getPNGFilesInDir(path))
                frames.add(new Image(new FileInputStream(file)));

        } catch (Exception e) {
            System.out.println("cannot load dust images / " + e.getMessage());
        }
        dust.setImage(frames.get(0));
        dust.setX(x);
        dust.setY(y);
        root.getChildren().add(dust);


        Timeline bomb = new Timeline();
        bomb.getKeyFrames().add(new KeyFrame(Duration.millis(40), event -> {
            dust.setImage(frames.get(frameIndex));
            frameIndex++;
            if (frameIndex == frames.size()) {
                root.getChildren().remove(dust);
                bomb.stop();
            }
        }));
        bomb.setCycleCount(Timeline.INDEFINITE);
        bomb.play();
    }
}
