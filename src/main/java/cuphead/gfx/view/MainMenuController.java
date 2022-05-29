package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.model.Database;
import cuphead.gfx.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class MainMenuController {
    @FXML
    private VBox list;
    @FXML
    private Button loadGame;
    @FXML
    private Button profile;


    public void initialize() {
        if (Database.getLoggedInUser() == null) {
            profile.setDisable(true);
            Hyperlink link = new Hyperlink("می توانید ثبت نام کنید یا وارد شوید.");
            link.setOnAction(event -> {
                Main.click.play();
                Main.gotoMenu("login-page");
            });
            list.getChildren().add(link);
        }
    }

    public void exitTheGame() {
        Main.click.play();
        System.exit(0);
    }

    public void gotoProfileMenu() {
        Main.click.play();
        Main.gotoMenu("profile-menu");
    }

    public void startGame() {
        Main.click.play();
        Main.gotoMenu("presetting-menu");
    }


    public void loadGame() {
        String username;
        if (Database.getLoggedInUser() == null)
            username = "guest";
        else
            username = Database.getLoggedInUser().getUsername();

        try {
            FileInputStream fileStream = new FileInputStream("./data/" + username + ".dat");
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);

            Database.getSavings().user = (User) objectStream.readObject();
            Database.getSavings().myLive = (double) objectStream.readObject();
            Database.getSavings().bossLive = (double) objectStream.readObject();
            Database.getSavings().vulnerability = (double) objectStream.readObject();
            Database.getSavings().power = (double) objectStream.readObject();
            Database.getSavings().elapsedTime = (long) objectStream.readObject();

            objectStream.close();
            fileStream.close();
            Database.setHaveLoad(true);
            Main.gotoMenu("game");
        } catch (Exception e) {
            String message = "کاربر با نام کاربری " + username + "، بازی ای ذخیره نکرده است.";
            Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
            alert.showAndWait();
            loadGame.setDisable(true);
        }
    }

    public void showScores() {
        Main.click.play();
        Main.gotoMenu("score-table");
    }
}
