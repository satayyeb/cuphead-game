package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.controller.Controller;
import cuphead.gfx.model.Database;
import cuphead.gfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SignupPageController {
    @FXML
    private TextField password2;
    @FXML
    private Text usernameError;
    @FXML
    private Text passwordError;
    @FXML
    private Text error;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    public void userPassCheck(KeyEvent keyEvent) {
        error.setText("");
        if (keyEvent.getCode().toString().equals("ENTER"))
            login();
        if (Database.getUser(username.getText()) != null)//username already taken
            usernameError.setText("قبلا کاربری با این نام کاربری ثبت نام کرده است.");
        else
            usernameError.setText("");

        if (Controller.isPasswordWeak(password.getText()))
            passwordError.setText("گذرواژه ضعیف است.");
        else
            passwordError.setText("");
    }

    @FXML
    private void login() {
        Main.click.play();
        if (username.getText().isBlank()) {
            error.setText("یک نام کاربری انتخاب کنید.");
            return;
        }
        if (password.getText().isBlank()) {
            error.setText("یک گذرواژه انتخاب کنید.");
            return;
        }
        if (!usernameError.getText().isBlank()) {
            error.setText("یک نام کاربری دیگری انتخاب کنید.");
            return;
        }
        if (!passwordError.getText().isBlank()) {
            error.setText("گذرواژه انتخاب شده بسیار ضعیف است.");
            return;
        }
        if (!password.getText().equals(password2.getText())) {
            error.setText("گذرواژه در دو قسمت متفاوت وارد شده است.");
            return;
        }
        User user = new User(username.getText(), password.getText(), getRandomAvatar());
        Database.addUser(user);
        Database.setLoggedInUser(user);
        error.setText("شما با موفقیت ثبت نام شدید.");
        Main.gotoMenu("main-menu");
    }

    private String getRandomAvatar() {
        try {
            List<String> filePaths = Controller.getPNGFilesInDir("/cuphead/gfx/media/avatars/");
            Random random = new Random(System.currentTimeMillis());
            return filePaths.get(random.nextInt(filePaths.size()));
        } catch (Exception e) {
            System.out.println("Can not pick a random avatar / " + e.getMessage());
        }
        return null;
    }

    public void enter(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            Main.click.play();
            login();
        }
    }

    public void gotoLoginPage() {
        Main.click.play();
        Main.gotoMenu("login-page");
    }
}
