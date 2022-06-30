package sh.shinterface.screen;

import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import sh.shinterface.Main;
import sh.shinterface.Settings;

import java.util.List;

public class StartScreen extends TitledScreen {

    public StartScreen() {
        super("SECRET HITLER");
        Button newGame = new Button("New Game");
        newGame.setOnAction(Main::newGame);
        Button loadGame = new Button("Load Game");
        loadGame.setOnAction(Main::loadGame);
        Button settings = new Button("Settings");
        ToggleButton switchButton = new ToggleButton("OFF");
        switchButton.textProperty().bind(Bindings.createStringBinding(() -> switchButton.isSelected() ? "ON" : "OFF", switchButton.selectedProperty()));
        switchButton.setSelected(Settings.styleSwitchEnabled());
        switchButton.setOnAction(e -> Settings.setStyleSwitch(switchButton.isSelected()));

        List<Node> buttons = List.of(newGame, loadGame, settings, switchButton);
        List<String> descriptions = List.of("Start a new game", "Load a finished game", "Open Settings", "Liberal/Fascist color switch");
        GridPane gridPane = new GridPane();
        for (int i = 0; i < buttons.size(); i++) {
            gridPane.addRow(i, buttons.get(i), new Label(descriptions.get(i)));
            GridPane.setHalignment(buttons.get(i), HPos.RIGHT);
        }
        this.getChildren().add(0, gridPane);

        gridPane.getStyleClass().add("start-menu");
        super.getStylesheets().add("sh/shinterface/stylesheets/menu.css");
    }

}
