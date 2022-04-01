package sh.shinterface;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ConfigScreen.display();
    }

    public static void main(String[] args) {
        launch(args);
    }
}