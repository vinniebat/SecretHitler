package sh.shinterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sh.shinterface.config.ConfigScreen;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.game.Game;
import sh.shinterface.game.component.GovSpecifics;

import java.util.List;

public class Main extends Application {

    private static Stage stage;

    private static ConfigScreen configScreen;

    /**
     * Checks if the configuration input is valid and makes a new game if it is.
     *
     * @param e Unused
     */
    public static void confirmSelection(ActionEvent e) {
        List<Player> players = configScreen.getPlayers();
        if (!players.isEmpty()) {
            //later factory
            //TODO add activePlayer
            stage.close();
            Game game = new Game(players, configScreen.getRole());
            game.start(stage);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Main.stage = stage;
//        configScreen = new ConfigScreen(stage);
//        Scene scene = new Scene(configScreen);
        Scene scene = new Scene(new GovSpecifics(new TableView<>()));
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.setTitle("Secret Hitler Interface");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/secret-hitler-icon4.png")));
        stage.show();
    }
}