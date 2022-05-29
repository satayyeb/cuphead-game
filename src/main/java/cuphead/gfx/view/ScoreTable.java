package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.model.Database;
import cuphead.gfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreTable {
    @FXML
    private VBox list;

    public void initialize() {
        List<User> users = Database.getUsers();
        users.sort(Comparator.comparing(User::getScore).reversed());
        int index = 1;
        for (User user : users) {
            Text record = new Text(index + ". کاربر " + user.getUsername() + " با امتیاز: " + user.getScore());
            record.setStyle("-fx-font-family: 'B Titr';-fx-font-size: 20");
            list.getChildren().add(record);
            index++;
            if (index == 11)
                break;
        }
        Button back = new Button("بازگشت");
        back.setOnAction(event -> {
            Main.click.play();
            Main.gotoMenu("main-menu");
        });
        list.getChildren().add(back);
    }
}
