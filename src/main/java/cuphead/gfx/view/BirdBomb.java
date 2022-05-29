package cuphead.gfx.view;

import cuphead.gfx.Main;
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

public class BirdBomb  implements Serializable {
    private final ImageView dust = new ImageView();
    private final List<Image> frames = new ArrayList<>();
    private int frameIndex = 0;

    public BirdBomb(final Pane root, double x, double y) {
        try {
            for (String file : Controller.getPNGFilesInDir("/cuphead/gfx/media/miniBossDeath/"))
                frames.add(new Image(Main.getResource(file)));
        } catch (Exception e) {
            System.out.println("cannot load dust images / " + e.getMessage());
        }
        dust.setImage(frames.get(0));
        dust.setX(x);
        dust.setY(y);
        root.getChildren().add(dust);

        Timeline birdBomb = new Timeline();
        birdBomb.getKeyFrames().add(new KeyFrame(Duration.millis(70), event -> {
            dust.setImage(frames.get(frameIndex));
            frameIndex++;
            if (frameIndex == frames.size()) {
                root.getChildren().remove(dust);
                birdBomb.stop();
            }
        }));
        birdBomb.setCycleCount(Timeline.INDEFINITE);
        birdBomb.play();
    }
}
