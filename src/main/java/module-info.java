module cuphead.gfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
//    opens cuphead.gfx to javafx.media;


    opens cuphead.gfx to javafx.fxml;
    exports cuphead.gfx;
    exports cuphead.gfx.view;
    opens cuphead.gfx.view to javafx.fxml;
}