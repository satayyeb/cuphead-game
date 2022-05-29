package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniBossGroup  implements Serializable {
    private final int numberOfBirds = 4;
    private List<ImageView> miniBosses = new ArrayList<>();
    private final List<Image> frames = new ArrayList<>();


    public MiniBossGroup(Pane root) {
        try {
            for (File file : Controller.getPNGFilesInDir("./media/miniBossYellow/"))
                frames.add(new Image(new FileInputStream(file)));
        } catch (Exception e) {
            System.out.println("cannot load miniBoss image / " + e.getMessage());
        }

        Random random = new Random(System.currentTimeMillis());
        int x = (int) (Main.getScene().getWidth() + 50);
        int y = random.nextInt(0, 3) * 200 + 100;

        for (int i = 0; i < numberOfBirds; i++) {
            ImageView miniBoss = new ImageView(frames.get(i % (frames.size() - 1)));
            miniBoss.setX(x + 180 * i);
            miniBoss.setY(y);
            miniBosses.add(miniBoss);
        }

        root.getChildren().addAll(miniBosses);

        Timeline flight = new Timeline();
        flight.getKeyFrames().add(new KeyFrame(Duration.millis(70), event -> {
            for (ImageView bird : miniBosses) {
                Image currentFrame = bird.getImage();
                int index = frames.indexOf(currentFrame) + 1;
                if (index == frames.size())
                    index = 0;
                bird.setImage(frames.get(index));
            }
        }));
        flight.setCycleCount(Timeline.INDEFINITE);
        flight.play();
        for (ImageView bird : miniBosses) {
            TranslateTransition move = new TranslateTransition();
            move.setNode(bird);
            move.setByX(-2500);
            move.setDuration(Duration.millis(7000));
            move.play();
            move.setOnFinished(event -> {
                move.stop();
                root.getChildren().remove(bird);
            });
        }
    }

    public List<ImageView> getMiniBosses() {
        return miniBosses;
    }
}
