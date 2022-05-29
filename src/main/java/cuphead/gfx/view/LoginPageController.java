package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.model.Database;
import cuphead.gfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;

import java.io.File;

public class LoginPageController {
    @FXML
    private Text error;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    public void login() {
        Main.click.play();
        if (username.getText().isBlank()) {
            error.setText("نام کاربری را وارد کنید.");
            return;
        }
        if (password.getText().isBlank()) {
            error.setText("گذرواژه را وارد کنید.");
            return;
        }
        User user = Database.getUser(username.getText());
        if (user == null) {
            error.setText("کاربری با این نام کاربری وجود ندارد.");
            return;
        }
        if (!user.getPassword().equals(password.getText())) {
            error.setText("گذرواژه نادرست است.");
            return;
        }
        Database.setLoggedInUser(user);
        error.setText("شما با موفقیت وارد شدید.");
        Main.gotoMenu("main-menu");
    }

    public void checkEnter(KeyEvent keyEvent) {
        error.setText("");
        if (keyEvent.getCode().toString().equals("ENTER"))
            login();
    }

    public void gotoSignupMenu() {
        Main.click.play();
        Main.gotoMenu("signup-page");
    }

    public void guest() {
        Main.click.play();
        Main.gotoMenu("main-menu");
    }
}
