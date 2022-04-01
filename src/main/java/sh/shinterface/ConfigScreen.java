package sh.shinterface;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen {

    private static final int minPlayers = 5;
    private static final int maxPlayers = 10;
    private static List<HBox> playerFields = new ArrayList<>();
    private static Stage stage;
    private static final Pane playersContainer = new VBox();

    public static void display() {
        Stage stage = new Stage();
        StackPane stackPane = new StackPane();
        createContent(stackPane, stage);
        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.show();
    }

    private static void createContent(StackPane stackPane, Stage stage) {
        ConfigScreen.stage = stage;
        VBox vBox = new VBox();
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        for (int i = minPlayers; i < maxPlayers; i++) {
            choiceBox.getItems().add(i);
        }
        choiceBox.setValue(minPlayers);
        choiceBox.setOnAction(ConfigScreen::updatePlayers);
        choiceBox.fireEvent(new ActionEvent());

        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> create(stage));

        vBox.getChildren().addAll(choiceBox, playersContainer, createGameButton);
        stackPane.getStyleClass().add("selection-screen");
        Label title = new Label("SECRET HITLER");
        title.getStyleClass().add("title");
        stackPane.getChildren().addAll(vBox, title);
    }

    private static void updatePlayers(ActionEvent e) {
        for (HBox hbox : playerFields) {
            TextField field = (TextField) hbox.getChildren().get(1);
            field.clear();
        }
        int amount = ((ChoiceBox<Integer>) e.getSource()).getValue();
        if (amount > playerFields.size()) {
            for (int i =  1 + playerFields.size(); i <= amount; i++) {
                playerFields.add(
                        new HBox(
                                new Label("Speler " + i + ":"),
                                new TextField()
                        )
                );
            }
        } else {
            playerFields = playerFields.subList(0, amount);
        }
        playersContainer.getChildren().clear();
        playersContainer.getChildren().addAll(playerFields);
        stage.sizeToScene();
    }

    private static void create(Stage stage) {
        boolean valid = true;
        ArrayList<Player> players = new ArrayList<>();

        int i;
        for (i = 0; i < playerFields.size(); i++) {
            HBox PlayerField = playerFields.get(i);
            TextField currentTextField = (TextField) PlayerField.getChildren().get(1);
            String name = currentTextField.getText().trim();
            if (name.isBlank()) {
                valid = false;
                PlayerField.getStyleClass().add("emptyField");
            } else {
                players.add(new Player(i + 1, name));
                PlayerField.getStyleClass().removeAll("emptyField");
            }
        }
        if (valid) {
            //later factory
            stage.close();
            Game game = new fivePlayerGame(players);
            game.start();
        }
    }
}
