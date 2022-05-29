package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.controller.Controller;
import cuphead.gfx.model.Database;
import cuphead.gfx.model.Savings;
import cuphead.gfx.model.User;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameController implements Serializable {
    @FXML
    private VBox upperHead;
    @FXML
    private VBox center;
    @FXML
    private HBox liveBar;
    @FXML
    private VBox head;
    @FXML
    private BorderPane pane;
    private ImageView cuphead = null;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;
    private long lastFrameTime;
    private final List<ImageView> bullets = new ArrayList<>();
    private final List<MiniBossGroup> miniBossGroups = new ArrayList<>();
    private List<Egg> eggs = new ArrayList<>();
    private boolean shooting;
    private Double myLive = 3.0;
    private double bossLive = 100;
    private double vulnerability;
    private double power;
    private boolean blinking;
    private long startTime = System.currentTimeMillis();
    private boolean endGame;
    private Music music;
    private Rectangle navbar;

    public void cleanupMiniBossGroups() {
        for (MiniBossGroup group : miniBossGroups) {
            boolean remove = true;
            for (ImageView bird : group.getMiniBosses()) {
                if (pane.getChildren().contains(bird))
                    remove = false;
            }
            if (remove)
                miniBossGroups.remove(group);
        }
    }

    public void initialize() throws FileNotFoundException {
        switch (Database.getDifficulty()) {
            case 0 -> {
                myLive = 10.0;
                vulnerability = 0.5;
                power = 1.5;
            }
            case 1 -> {
                myLive = 5.0;
                vulnerability = 1;
                power = 1;
            }
            case 2 -> {
                myLive = 2.0;
                vulnerability = 1.5;
                power = 0.5;
            }
        }

        //load background image
        ImageView background = new ImageView(new Image(new FileInputStream("./media/background.png")
                , Main.getStage().getWidth(), Main.getStage().getHeight(), false, true));
        pane.getChildren().add(background);


        //loadGame
        if (Database.isHaveLoad()) {
            Database.setHaveLoad(false);
            bossLive = Database.getSavings().bossLive;
            myLive = Database.getSavings().myLive;
            power = Database.getSavings().power;
            vulnerability = Database.getSavings().vulnerability;
            startTime = System.currentTimeMillis() - Database.getSavings().elapsedTime;
        }

        //////upper navbar//////
        navbar = new Rectangle();
        int navbarWidth = 400;
        int navbarHeight = 80;
        navbar.setX(Main.getScene().getWidth() / 2 - navbarWidth / 2);
        navbar.setWidth(navbarWidth);
        navbar.setY(0);
        navbar.setHeight(navbarHeight);
        navbar.setFill(Color.rgb(225, 166, 255, .8));
        pane.getChildren().add(navbar);


        //Timer of the game and Live information
        Text timer = new Text("00:00");
        timer.setX(600);
        timer.setY(25);
        head.setViewOrder(-1);
        head.getChildren().add(timer);
        final Timeline timerTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            updateLiveInformation();
            long diff = System.currentTimeMillis() - startTime;
            String time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(diff),
                    TimeUnit.MILLISECONDS.toSeconds(diff) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
            );
            timer.setText("زمان سپری شده: " + time);
        }));
        timerTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.play();


        //////-//////


        //music configuration
        Main.music.stop();
        music = new Music("./media/track1.mp3", true);

        //load cuphead images
        List<Image> planeFrames = new ArrayList<>();
        try {
            for (File file : Controller.getPNGFilesInDir("./media/cuphead/"))
                planeFrames.add(new Image(new FileInputStream(file)));
        } catch (Exception e) {
            System.out.println("cannot load cuphead image / " + e.getMessage());
            System.exit(-1);
        }
        cuphead = new ImageView(planeFrames.get(0));
        cuphead.setY(200);
        cuphead.setScaleX(.9);
        cuphead.setScaleY(.9);
        pane.getChildren().add(cuphead);
        cuphead.setFocusTraversable(true);
        cuphead.setViewOrder(-1);

        //create the boss
        Boss boss = new Boss(pane, eggs, power);


        //create the miniBosses
        Timeline miniBossTimer = new Timeline(new KeyFrame(Duration.millis(3000 * power), event ->
                miniBossGroups.add(new MiniBossGroup(pane))));
        miniBossTimer.setCycleCount(Animation.INDEFINITE);
        miniBossTimer.play();


        //plane animation
        Timeline planeTimeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            Image currentFrame = cuphead.getImage();
            int index = planeFrames.indexOf(currentFrame) + 1;
            if (index == planeFrames.size())
                index = 0;
            cuphead.setImage(planeFrames.get(index));
        }));
        planeTimeline.setCycleCount(Animation.INDEFINITE);
        planeTimeline.setAutoReverse(true);
        planeTimeline.play();


        AnimationTimer animation = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //check win or lose
                if (!endGame) {
                    if (myLive <= 0)
                        end(false);
                    if (bossLive <= 0)
                        end(true);
                }


                //check the hits in the game
                for (MiniBossGroup group : miniBossGroups) {
                    group.getMiniBosses().removeIf(bird -> checkHitBirdCuphead(cuphead, bird));
                    group.getMiniBosses().removeIf(bird -> checkHitBirdBullet(bullets, bird));
                }
                checkHitCupheadBoss(cuphead, boss);
                checkHitCupheadEgg(cuphead);


                int moveAmount = 7;
                if (shooting && (now - lastFrameTime) > 200000000) {
                    lastFrameTime = now;
                    try {
                        Image bulletImage = new Image(new FileInputStream("./media/bullet.png"));
                        ImageView bullet = new ImageView(bulletImage);
                        bullet.setViewOrder(0);
                        Random random = new Random();
                        bullet.setX(cuphead.getX() + 60);
                        bullet.setY(cuphead.getY() + random.nextInt(40, 70));
                        pane.getChildren().add(bullet);
                        bullets.add(bullet);
                        Music music = new Music("./media/shot.mp3", false);
                        music.setVolume(0.1);

                    } catch (Exception e) {
                        System.out.println("cannot load bullet image / " + e.getMessage());
                    }
                }

                List<ImageView> removalBullets = new ArrayList<>();
                for (ImageView bullet : bullets) {
                    bullet.setX(bullet.getX() + 2 * moveAmount);
                    if (bullet.getX() > Main.getStage().getWidth()) {
                        pane.getChildren().remove(bullet);
                        removalBullets.add(bullet);
                    } else if (bullet.getX() > boss.getView().getX() + 110 &&
                            bullet.getY() > boss.getView().localToScene(boss.getView().getBoundsInLocal()).getMinY() + 30 &&
                            bullet.getY() < boss.getView().localToScene(boss.getView().getBoundsInLocal()).getMinY() + 270) {
                        pane.getChildren().remove(bullet);
                        removalBullets.add(bullet);
                        new Dust(pane, bullet.getX(), bullet.getY(), false);
                        bossLive -= power;
                    }
                }
                bullets.removeAll(removalBullets);

                if (moveUp)
                    cuphead.setY(cuphead.getY() - moveAmount);
                if (moveDown)
                    cuphead.setY(cuphead.getY() + moveAmount);
                if (moveRight)
                    cuphead.setX(cuphead.getX() + moveAmount);
                if (moveLeft)
                    cuphead.setX(cuphead.getX() - moveAmount);
                if (cuphead.getX() < 0) {
                    cuphead.setX(0);
                    moveLeft = false;
                }
                if (cuphead.getY() < 0) {
                    cuphead.setY(0);
                    moveUp = false;
                }
                if (cuphead.getX() > Main.getScene().getWidth() - cuphead.getImage().getWidth()) {
                    cuphead.setX(Main.getScene().getWidth() - cuphead.getImage().getWidth());
                    moveRight = false;
                }
                if (cuphead.getY() > Main.getScene().getHeight() - cuphead.getImage().getHeight()) {
                    cuphead.setY(Main.getScene().getHeight() - cuphead.getImage().getHeight());
                    moveDown = false;
                }
            }
        };
        animation.start();


        cuphead.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP -> moveUp = true;
                    case DOWN -> moveDown = true;
                    case LEFT -> moveLeft = true;
                    case RIGHT -> moveRight = true;
                    case SPACE -> shooting = true;
                    case S -> saveGame();
                }
            }
        });

        cuphead.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP -> moveUp = false;
                    case DOWN -> moveDown = false;
                    case LEFT -> moveLeft = false;
                    case RIGHT -> moveRight = false;
                    case SPACE -> shooting = false;
                }
            }
        });
    }

    private void saveGame() {
        try {
            String username;
            if (Database.getLoggedInUser() == null)
                username = "guest";
            else
                username = Database.getLoggedInUser().getUsername();
            FileOutputStream fileStream = new FileOutputStream("./data/" + username + ".dat");
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(Database.getLoggedInUser());
            objectStream.writeObject(myLive);
            objectStream.writeObject(bossLive);
            objectStream.writeObject(vulnerability);
            objectStream.writeObject(power);
            objectStream.writeObject(System.currentTimeMillis() - startTime);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            System.out.println("An Error occurred during saving game / " + e.getMessage());
        }
    }

    private void end(boolean win) {
        long score;
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        Text text;
        if (win) {
            score = 2400 / elapsedTime;
            text = new Text("شما برنده شدید! امتیاز: " + score);
        } else {
            score = -120000 / (System.currentTimeMillis() - startTime);
            text = new Text("شما باختی! امتیاز: منفی " + -score);
        }
        text.setStyle("-fx-font-size: 60; -fx-font-family: 'B Titr';");
        text.setViewOrder(0);
        upperHead.getChildren().add(new Text());
        upperHead.getChildren().add(text);

        Text time = new Text("مدت زمان بازی: " + elapsedTime + " ثانیه.");
        time.setStyle("-fx-font-size: 30; -fx-font-family: 'B Titr';");
        time.setViewOrder(0);
        upperHead.getChildren().add(time);

        Button button = new Button("اتمام بازی");
        button.setOnMouseClicked(event -> Main.gotoMenu("main-menu"));
        button.setViewOrder(0);
        upperHead.getChildren().add(button);

        Button playAgain = new Button("دوباره تلاش بکن!");
        playAgain.setOnMouseClicked(event -> Main.gotoMenu("presetting-menu"));
        playAgain.setViewOrder(0);
        upperHead.getChildren().add(playAgain);

        endGame = true;

        //score adding
        User user = Database.getLoggedInUser();
        if (user != null) {
            user.setScore((int) (user.getScore() + score));
        }
        music.stop();
        shooting = false;
        Main.music.play();
    }

    private void updateLiveInformation() {
        liveBar.getChildren().clear();

        Text myLiveText = new Text("❤" + "شما: " + myLive);
        if (myLive <= 2)
            navbar.setFill(Color.rgb(225, 20, 20, .8));
        liveBar.getChildren().add(myLiveText);

        Text bossLiveText = new Text("❤" + "دشمن: " + bossLive);
        liveBar.getChildren().add(bossLiveText);
    }


    private void cupheadDamage(ImageView cuphead) {
        myLive -= vulnerability;
        FadeTransition fade = new FadeTransition(Duration.millis(100));
        fade.setFromValue(100);
        fade.setToValue(0.1);
        fade.setCycleCount(10);
        fade.setAutoReverse(true);
        fade.setNode(cuphead);
        fade.play();
        blinking = true;
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(.8);
        colorAdjust.setContrast(.6);
        cuphead.setEffect(colorAdjust);
        fade.setOnFinished(event -> {
            blinking = false;
            cuphead.setEffect(null);
        });
    }

    private void checkHitCupheadBoss(ImageView cuphead, Boss boss) {
        if (blinking)
            return;
        if (cuphead.getX() > boss.getView().getX() + 70 &&
                cuphead.getY() > boss.getView().localToScene(boss.getView().getBoundsInLocal()).getMinY() + 10 &&
                cuphead.getY() < boss.getView().localToScene(boss.getView().getBoundsInLocal()).getMinY() + 270) {
            cupheadDamage(cuphead);
        }
    }

    private void checkHitCupheadEgg(ImageView cuphead) {
        if (blinking)
            return;
        for (Egg egg : eggs) {
            int eggX = (int) egg.getView().localToScene(egg.getView().getBoundsInLocal()).getMinX();
            int eggY = (int) egg.getView().getY();
            if ((eggX > cuphead.getX() - egg.getView().getImage().getWidth()) && eggX < -40 + cuphead.getX() + cuphead.getImage().getWidth() &&
                    eggY > cuphead.getY() - 80 && eggY < -50 + cuphead.getY() + cuphead.getImage().getHeight()) {
                cupheadDamage(cuphead);
                new Dust(pane, eggX, eggY, true);
                return;
            }
        }
    }

    private boolean checkHitBirdCuphead(ImageView cuphead, ImageView bird) {
        if (blinking)
            return false;
        int birdX = (int) bird.localToScene(bird.getBoundsInLocal()).getMinX();
        int birdY = (int) bird.getY();
        if ((birdX > cuphead.getX() - bird.getImage().getWidth()) && birdX < -40 + cuphead.getX() + cuphead.getImage().getWidth() &&
                birdY > cuphead.getY() - 80 && birdY < -50 + cuphead.getY() + cuphead.getImage().getHeight()) {
            cupheadDamage(cuphead);
            pane.getChildren().remove(bird);
            new Dust(pane, birdX, birdY, true);
            return true;
        }
        return false;
    }


    private boolean checkHitBirdBullet(List<ImageView> cuphead, ImageView bird) {
        int birdX = (int) bird.localToScene(bird.getBoundsInLocal()).getMinX();
        int birdY = (int) bird.getY();
        for (ImageView bullet : bullets) {
            if (bullet.getY() > birdY && bullet.getY() < birdY + birdY + bird.getFitHeight() &&
                    bullet.getX() > birdX && bullet.getX() < birdX + 100) {
                new Dust(pane, bullet.getX(), bullet.getY(), false);
                bullets.remove(bullet);
                pane.getChildren().removeAll(bird, bullet);
                new Dust(pane, birdX, birdY - 40, true);
                return true;
            }
        }
        return false;
    }
}
