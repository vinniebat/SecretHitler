package sh.shinterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sh.shinterface.config.ConfigScreen;
import sh.shinterface.game.Game;

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
            Game game = new Game(configScreen.getPlayers(), configScreen.getActivePlayer());
            game.start(stage);
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
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.setTitle("Secret Hitler Interface");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icons/secret-hitler-icon.png")));
        stage.show();
    }


}