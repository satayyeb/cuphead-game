package cuphead.gfx;

import cuphead.gfx.view.Music;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class Main extends Application {
    public static final Music click = new Music("/cuphead/gfx/media/slider.mp3", false);
    public static final Music music = new Music("/cuphead/gfx/media/track2.mp3", true);
    private static final Scene scene = new Scene(loadFxml("login-page"));
    private static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    public static String getResource(String name) {
        return Objects.requireNonNull(Main.class.getResource(name)).toExternalForm();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Cuphead");
        stage.setScene(scene);
        try {
            stage.getIcons().add(new Image(String.valueOf(Main.class.getResource("/cuphead/gfx/media/icon.png"))));
        } catch (Exception e) {
            System.out.println("cannot load icon / " + e.getMessage());
        }
        stage.setMaximized(true);
        Main.stage = stage;
        stage.show();
//        gotoMenu("game");
    }

    public static void gotoMenu(String name) {
        try {
            Parent parent = loadFxml(name);
            scene.setRoot(parent);
        } catch (Exception e) {
            System.out.println("can not load this page: " + name);
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static Parent loadFxml(String name) {
        try {
//            URL url = new URL(Objects.requireNonNull(Main.class.getResource("/cuphead/gfx/fxml/" + name + ".fxml")).toString());
//            return FXMLLoader.load(url);
//            String path = Main.getResource("/cuphead/gfx/fxml/" + name + ".fxml");
            return FXMLLoader.load(new URL(getResource("/cuphead/gfx/fxml/" + name + ".fxml")));
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }
}
