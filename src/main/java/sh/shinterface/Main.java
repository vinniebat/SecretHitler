package sh.shinterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sh.shinterface.screen.ConfigScreen;
import sh.shinterface.screen.Game;

import java.io.InputStream;

public class Main extends Application {

    private static Stage stage;

    private static ConfigScreen configScreen;

    /**
     * Checks if the configuration input is valid and makes a new game if it is.
     *
     * @param e Unused
     */
    public static void confirmSelection(ActionEvent e) {
        if (configScreen.isValid()) {
            //later factory
            stage.getScene().setRoot(new Game(configScreen.getPlayers(), configScreen.getActivePlayer()).getGameWindow());
            if (!stage.isMaximized() && !stage.isFullScreen()) {
                stage.sizeToScene();
                stage.centerOnScreen();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Main.stage = stage;
        configScreen = new ConfigScreen(stage);
        stage.setScene(new Scene(configScreen));
        stage.setTitle("Secret Hitler Interface");
        InputStream icon = getClass().getResourceAsStream("images/icons/secret-hitler-icon.png");
        assert icon != null; // If this failed the icon is missing
        stage.getIcons().add(new Image(icon));
        stage.show();
    }


}