package sh.shinterface.screen;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import sh.shinterface.Main;
import sh.shinterface.Settings;

import java.io.IOException;

public class StartScreen extends TitledScreen {

    @FXML
    private ToggleButton switchButton;

    public StartScreen() {
        super("SECRET HITLER");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startscreen.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            // exception.printStackTrace();
            System.err.println("Could not load configscreen.fxml");
            Platform.exit();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        switchButton.textProperty().bind(Bindings.createStringBinding(() -> switchButton.isSelected() ? "ON" : "OFF", switchButton.selectedProperty()));
        switchButton.setSelected(Settings.styleSwitchEnabled());
    }

    public void newGame(ActionEvent event) {
        Main.newGame();
    }

    public void loadGame(ActionEvent event) {
        Main.loadGame();
    }

    public void settings(ActionEvent event) {}

    public void toggleStyleSwitch(ActionEvent event) {
        Settings.setStyleSwitch(switchButton.isSelected());
    }

}
