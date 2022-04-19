package sh.shinterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sh.shinterface.config.ConfigScreen;
import sh.shinterface.game.Game;

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
            stage.close();
            stage.setScene(new Game(configScreen.getPlayers(), configScreen.getActivePlayer()));
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Main.stage = stage;
        configScreen = new ConfigScreen(stage);
        Scene scene = new Scene(configScreen);
        scene.getStylesheets().add("sh/shinterface/stylesheets/configscreen.css");
        stage.setScene(scene);
        stage.setTitle("Secret Hitler Interface");
        InputStream icon = getClass().getResourceAsStream("images/icons/secret-hitler-icon.png");
        assert icon != null; // If this failed the icon is missing
        stage.getIcons().add(new Image(icon));
        stage.show();
    }


}