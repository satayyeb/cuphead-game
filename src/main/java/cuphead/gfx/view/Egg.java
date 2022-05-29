package cuphead.gfx.view;

import cuphead.gfx.Main;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class Egg  implements Serializable {
    private ImageView egg = new ImageView();

    public Egg(ImageView boss, Pane root, List<Egg> eggs) {
        try {
//            egg = new ImageView(new Image(new FileInputStream("./media/egg.png")));
            URL x = getClass().getResource("/cuphead/gfx/egg.png");
            egg = new ImageView(new Image(x.toExternalForm()));
        } catch (Exception e) {
            System.out.println("cannot load egg image / " + e.getMessage());
            System.exit(-1);
        }

        egg.setX(boss.getX() + 70);
        egg.setY(boss.localToScene(boss.getBoundsInLocal()).getMinY() + 120);
        egg.setScaleX(.8);
        egg.setScaleY(.8);
        root.getChildren().add(egg);

        RotateTransition rotate = new RotateTransition(Duration.millis(5000), egg);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setByAngle(360 * 5);
        rotate.play();

        TranslateTransition move = new TranslateTransition();
        move.setNode(egg);
        move.setByX(-1200);
        move.setDuration(Duration.millis(2000));
        move.play();
        move.setOnFinished(event -> {
            move.stop();
            rotate.stop();
            root.getChildren().remove(egg);
            eggs.remove(this);
        });
    }

    public ImageView getView() {
        return egg;
    }
}
