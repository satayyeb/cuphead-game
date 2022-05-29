package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.controller.Controller;
import cuphead.gfx.model.Database;
import cuphead.gfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileMenu {

    @FXML
    private VBox leftMenu;
    @FXML
    private VBox profileSettings;

    public void initialize() {
        User user = Database.getLoggedInUser();
        Text username = new Text(user.getUsername());
        Text score = new Text("امتیاز:  " + user.getScore());
        Text emptySpace = new Text();
        leftMenu.getChildren().add(0, username);
        leftMenu.getChildren().add(1, score);
        leftMenu.getChildren().add(2, emptySpace);
        try {
            Image image;
            try {
                image = new Image(Main.getResource(Database.getLoggedInUser().getAvatarPath()));
            } catch (Exception e) {
                image = new Image(Database.getLoggedInUser().getAvatarPath());
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            leftMenu.getChildren().add(0, imageView);
            Text emptySpace2 = new Text();
            leftMenu.getChildren().add(1, emptySpace2);
        } catch (Exception e) {
            System.out.println("Can not load image in left menu");
        }
    }

    public void returnToMainMenu() {
        Main.click.play();
        Main.gotoMenu("main-menu");
    }

    public void logout() {
        Main.click.play();
        Database.setLoggedInUser(null);
        Main.gotoMenu("login-page");
    }

    public void deleteAccount() {
        Main.click.play();
        profileSettings.getChildren().clear();
        String message = "آیا از حذف حساب کاربری خود، اطمینان دارید؟ با این کار همه اطلاعات حسابتان و امتیاز های کسب شده توسط شما، حذف خواهند شد.";
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle("هشدار");
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.YES) {
                Database.removeUser(Database.getLoggedInUser());
                logout();
            }
            Main.click.play();
        });
    }

    public void changeAvatar() {
        Main.click.play();
        profileSettings.getChildren().clear();
        try {
            Text title = new Text("برای خود یک آواتار انتخاب کنید.");
            title.setStyle("-fx-font-size: 36");
            profileSettings.getChildren().add(title);
            List<Image> avatars = new ArrayList<>();
            List<String> filePaths = Controller.getPNGFilesInDir("/cuphead/gfx/media/avatars/");
            for (String path : filePaths)
                avatars.add(new Image(Main.getResource(path)));
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            for (int i = 0; i < 10; i++) {
                ImageView imageView = new ImageView(avatars.get(i));
                imageView.setFitHeight(175);
                imageView.setFitWidth(175);
                Hyperlink link = new Hyperlink();
                link.setGraphic(imageView);
                int finalI = i;
                link.setOnMouseClicked(event -> {
                    Main.click.play();
                    Database.getLoggedInUser().setAvatarPath(filePaths.get(finalI));
                    leftMenu.getChildren().remove(0, 5);
                    initialize();
                });
                gridPane.add(link, i % 5, i / 5);
            }
            profileSettings.getChildren().add(gridPane);
            Hyperlink link = new Hyperlink("می توانید یک عکس را آپلود کنید.");
            link.setOnAction(event -> uploadImage());
            profileSettings.getChildren().add(link);
        } catch (Exception e) {
            System.out.println("Can not load all avatars for change avatar menu / " + e.getMessage());
        }
    }

    private void uploadImage() {
        Main.click.play();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Database.getLoggedInUser().setAvatarPath(file.getAbsolutePath());
            leftMenu.getChildren().remove(0, 5);
            initialize();
        }
    }

    public void changeUsername() {
        Main.click.play();
        profileSettings.getChildren().clear();
        Text title = new Text("تغییر نام کاربری");
        title.setStyle("-fx-font-size: 30;-fx-font-family: 'B Titr';");
        profileSettings.getChildren().add(title);

        Text emptySpace = new Text();
        profileSettings.getChildren().add(emptySpace);

        TextField username = new TextField();
        username.setPromptText("نام کاربری جدید خود را وارد کنید.");
        profileSettings.getChildren().add(username);

        Text error = new Text();
        error.setOnKeyReleased(event -> {
            if (Database.getUser(username.getText()) != null)
                error.setText("این نام کاربری قبلا توسط فرد دیگری ثبت شده است.");
            else
                error.setText("");
        });
        profileSettings.getChildren().add(error);

        Button submit = new Button("اعمال");
        submit.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE, checkUsername(username), ButtonType.OK);
            alert.setTitle("تغییر نام کاربری");
            alert.show();
        });
        profileSettings.getChildren().add(submit);
    }

    private String checkUsername(TextField username) {
        Main.click.play();
        if (username.getText().isBlank())
            return "یک نام کاربری جدید انتخاب کنید.";
        if (username.getText().equals(Database.getLoggedInUser().getUsername()))
            return "نام کاربری انتخاب شده همان نام کاربری پیشین است.";
        if (Database.getUser(username.getText()) != null)
            return "قبلا این نام کاربری توسط فرد دیگری انتخاب شده است.";
        Database.getLoggedInUser().setUsername(username.getText());
        return "نام کاربری شما با موفقیت تغییر یافت. نام کابری جدید:\n" + username.getText();
    }

    public void changePassword() {
        Main.click.play();
        profileSettings.getChildren().clear();

        Text title = new Text("تغییر گذرواژه");
        title.setStyle("-fx-font-size: 30;-fx-font-family: 'B Titr';");
        profileSettings.getChildren().add(title);

        Text emptySpace = new Text();
        profileSettings.getChildren().add(emptySpace);

        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("گذرواژه قبلی خود را وارد کنید.");
        profileSettings.getChildren().add(oldPassword);

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("گذرواژه جدید خود را وارد کنید.");
        profileSettings.getChildren().add(newPassword);

        PasswordField newPasswordRepeat = new PasswordField();
        newPasswordRepeat.setPromptText("گذرواژه جدید خود را دوباره وارد کنید.");
        profileSettings.getChildren().add(newPasswordRepeat);

        Button submit = new Button("اعمال");
        submit.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE, checkPassword(oldPassword, newPassword, newPasswordRepeat), ButtonType.OK);
            alert.setTitle("تغییر گذرواژه");
            alert.show();
        });
        profileSettings.getChildren().add(submit);
    }

    private String checkPassword(PasswordField oldPassword, PasswordField newPassword, PasswordField newPasswordRepeat) {
        Main.click.play();
        if (oldPassword.getText().isBlank())
            return "گذرواژه قبلی را وارد کنید";
        if (!oldPassword.getText().equals(Database.getLoggedInUser().getPassword()))
            return "گذرواژه قبلی نادرست است";
        if (newPassword.getText().isBlank())
            return "گذرواژه جدید را وارد کنید";
        if (newPassword.getText().equals(Database.getLoggedInUser().getPassword()))
            return "گذرواژه قبلی با جدید یکی است";
        if (Controller.isPasswordWeak(newPassword.getText()))
            return "گذرواژه جدید بسیار ضعیف است. یکی دیگر را امتحان کنید";
        if (!newPassword.getText().equals(newPasswordRepeat.getText()))
            return "گذرواژه جدید به درستی تکرار نشده است";
        Database.getLoggedInUser().setPassword(newPassword.getText());
        return "گذرواژه شما با موفقیت تغییر یافت";
    }
}
