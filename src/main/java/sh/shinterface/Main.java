package sh.shinterface;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sh.shinterface.config.ConfigScreen;
import sh.shinterface.game.Game;
import sh.shinterface.menu.StartMenu;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    private static Stage stage;

    private static ConfigScreen configScreen;

    /**
     * Checks if the configuration input is valid and makes a new game if it is.
     *
     * @param e Unused
     */
    public static void confirmSelection(Event e) {
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
        try {
            Settings.load();
            Main.stage = stage;
            Scene scene = new Scene(new StartMenu());
            stage.setScene(scene);
            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F11) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });
            ChangeListener<Boolean> resize = (o, oldB, newB) -> {
                if (!newB)
                    stage.sizeToScene();
            };

            ChangeListener<Boolean> fontSize = (o, oldB, newB) ->
                    stage.getScene().getRoot().setStyle("-fx-font-size: " + Screen.getPrimary().getBounds().getHeight() * ((newB) ? 0.03 : 0.01));
            stage.maximizedProperty().addListener(fontSize);
            stage.fullScreenProperty().addListener(fontSize);
            stage.maximizedProperty().addListener(resize);
            stage.fullScreenProperty().addListener(resize);
            stage.setTitle("Secret Hitler Interface");
            InputStream icon = getClass().getResourceAsStream("images/icons/secret-hitler-icon.png");
            assert icon != null; // If this failed the icon is missing
            stage.getIcons().add(new Image(icon));
            stage.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to load settings, default settings will be used and changes will not be saved.\nDo you wish to continue?", ButtonType.YES, ButtonType.NO)
                    .showAndWait()
                    .filter(ButtonType.NO::equals)
                    .ifPresent(b -> Platform.exit());
        }
    }

    public static void newGame(Event e) {
        configScreen = new ConfigScreen(stage);
        stage.getScene().setRoot(configScreen);
        if (!stage.isFullScreen() && !stage.isMaximized())
            stage.sizeToScene();
    }

    public static void loadGame(Event e) {
        //TODO: load a finished game
    }

    public void stop() {
        Settings.save();
    }
}