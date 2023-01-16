module cz.spsmb.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;


    opens cz.spsmb.snake to javafx.fxml;
    exports cz.spsmb.snake;
}