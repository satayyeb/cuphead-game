package cuphead.gfx.view;

import cuphead.gfx.Main;
import cuphead.gfx.model.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class PresettingMenu {
    @FXML
    private Label devilMode;
    @FXML
    private ComboBox<String> comboBox;

    public void initialize() {
        comboBox.getItems().add("آسان  (جان اولیه: 10 | آسیب پذیری: 50% | آسیب رسانی: 150%)");
        comboBox.getItems().add("متوسط (جان اولیه: 5 | آسیب پذیری: 100% | آسیب رسانی: 100%)");
        comboBox.getItems().add("دشوار (جان اولیه: 2 |  آسیب پذیری: 150% |  آسیب رسانی: 50%)");
        comboBox.getSelectionModel().select(1);

        CheckBox checkBox = new CheckBox();
        devilMode.setGraphic(checkBox);
        devilMode.setContentDisplay(ContentDisplay.RIGHT);
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                Main.click.play();
                comboBox.getSelectionModel().selectLast();
                comboBox.setDisable(true);
            } else {
                Main.click.play();
                comboBox.setDisable(false);
            }
        });
    }

    public void startGame() {
        Main.click.play();
        Database.setDifficulty(comboBox.getSelectionModel().getSelectedIndex());
        Main.gotoMenu("game");
    }

    public void back() {
        Main.click.play();
        Main.gotoMenu("main-menu");
    }

}
