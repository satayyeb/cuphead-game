package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.controller.Controller;
import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.*;

public class Boss  implements Serializable {
    private final ImageView boss = new ImageView();
    private final List<Image> bossFrames = new ArrayList<>();
    private final List<Image> bossShootFrames = new ArrayList<>();
    private final List<Egg> eggs;

    private void loadFrames() {
        try {
            for (File file : Controller.getPNGFilesInDir("./media/boss/"))
                bossFrames.add(new Image(new FileInputStream(file)));
            for (File file : Controller.getPNGFilesInDir("./media/bossShoot/"))
                bossShootFrames.add(new Image(new FileInputStream(file)));
        } catch (Exception e) {
            System.out.println("cannot load boss images / " + e.getMessage());
            System.exit(-1);
        }
        boss.setImage(bossFrames.get(0));
    }


    public Boss(Pane root, List<Egg> eggs, double power) {
        loadFrames();
        boss.setX(800);
        boss.setScaleX(.7);
        boss.setScaleY(.7);
        root.getChildren().add(boss);
        this.eggs = eggs;

        Timeline bossIdle = new Timeline();
        bossIdle.getKeyFrames().add(new KeyFrame(Duration.millis(55), event -> {
            Image currentFrame = boss.getImage();
            int index = bossFrames.indexOf(currentFrame) + 1;
            if (index == bossFrames.size())
                index = 0;
            boss.setImage(bossFrames.get(index));
        }));
        bossIdle.setCycleCount(Timeline.INDEFINITE);
        bossIdle.play();

        Timeline bossShoot = new Timeline();
        bossShoot.getKeyFrames().add(new KeyFrame(Duration.millis(55), event -> {
            Image currentFrame = boss.getImage();
            int index = bossShootFrames.indexOf(currentFrame) + 1;
            if (index == 1)
                boss.setX(670);
            if (index == 4)
                makeEgg(root);
            if (index == 9)
                boss.setX(800);
            if (index == bossShootFrames.size()) {
                bossShoot.stop();
                bossIdle.play();
                return;
            }
            boss.setImage(bossShootFrames.get(index));
        }));
        bossShoot.setCycleCount(Timeline.INDEFINITE);


        Timeline shootingTimer = new Timeline(new KeyFrame(Duration.millis(2500 * power), event -> {
            bossIdle.stop();
            bossShoot.play();
        }));
        shootingTimer.setCycleCount(Timeline.INDEFINITE);
        shootingTimer.play();

        //move up and down
        TranslateTransition bossMove = new TranslateTransition();
        bossMove.setNode(boss);
        bossMove.setFromY(-100);
        bossMove.setToY(Main.getScene().getHeight() - 400);
        bossMove.setDuration(Duration.millis(4000));
        bossMove.setCycleCount(Animation.INDEFINITE);
        bossMove.setAutoReverse(true);
        bossMove.play();
    }

    private void makeEgg(Pane root) {
        eggs.add(new Egg(boss, root, eggs));
    }

    public ImageView getView() {
        return boss;
    }
}
