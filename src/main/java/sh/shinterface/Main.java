package sh.shinterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    private static Stage stage;

    private static ConfigScreen configScreen;
    @Override
    public void start(Stage stage) {
        Main.stage = stage;
        configScreen = new ConfigScreen(stage);
        Scene scene = new Scene(configScreen);
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.setTitle("Secret Hitler Interface");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("secret-hitler-icon.png")));
        stage.show();
    }

    /**
     * Checks if the configuration input is valid and makes a new game if it is.
     * @param e Unused
     */
    public static void confirmSelection(ActionEvent e) {
        List<Player> players = configScreen.getPlayers();
        if (!players.isEmpty()) {
            //later factory
            stage.close();
            Game game = new FivePlayerGame(players);
            game.start();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}