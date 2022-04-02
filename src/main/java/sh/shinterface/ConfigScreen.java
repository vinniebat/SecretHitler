package sh.shinterface;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration screen that handles the selection of amount of players and inputting the player names.
 */
public class ConfigScreen {

    /**
     * Minimum amount of players
     */
    private static final int minPlayers = 5;

    /**
     * Max amount of players
     */
    private static final int maxPlayers = 10;

    /**
     * Stage where the ConfigScreen is present
     */
    private static Stage stage;

    /**
     * Container that holds all the player fields. A player field consists of a Label (index 0) and TextField (index 1).
     */
    private static final Pane playersContainer = new VBox();

    /**
     * Displays the ConfigScreen
     */
    public static void display() {
        Stage stage = new Stage();
        StackPane stackPane = new StackPane();
        createContent(stackPane, stage);
        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * creates the content of the ConfigScreen
     * @param stackPane Pane that should hold the content
     * @param stage Stage that the holds the ConfigScreen
     */
    private static void createContent(StackPane stackPane, Stage stage) {
        ConfigScreen.stage = stage;
        VBox vBox = new VBox();
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        for (int i = minPlayers; i <= maxPlayers; i++) {
            choiceBox.getItems().add(i);
        }
        choiceBox.setValue(minPlayers);
        choiceBox.setOnAction(ConfigScreen::updatePlayers);
        choiceBox.fireEvent(new ActionEvent()); // Roept meteen updatePlayers op

        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> create(stage));

        vBox.getChildren().addAll(choiceBox, playersContainer, createGameButton);
        stackPane.getStyleClass().add("selection-screen");
        Label title = new Label("SECRET HITLER");
        title.getStyleClass().add("title");
        stackPane.getChildren().addAll(vBox, title);
    }

    /**
     * Updates the display to reflect the change in amount of players.
     * @param e event coming from the ChoiceBox
     */
    private static void updatePlayers(ActionEvent e) {
        ObservableList<Node> playerFields = playersContainer.getChildren();
        for (Node playerField : playerFields) {
            // fk u kris ik doe casting zoveel ik wil
            // We weten da de elementen in de playersContainer HBoxen zijn met daarin éérst een Label en dan een TextField
            ((TextInputControl) ((Pane) playerField).getChildren().get(1)).clear(); // We vegen dus gewoon het textField uit
            playerField.getStyleClass().removeAll("emptyField"); // Reset de error
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
        }
        List<Node> list = new ArrayList<>(playerFields.subList(amount, playerFields.size()));
        playersContainer.getChildren().removeAll(list);
        stage.sizeToScene();
    }

    /**
     * Checks if all fields where filled in and starts the game.
     * @param stage Stage that holds the ConfigScreen
     */
    private static void create(Stage stage) {
        boolean valid = true;
        ArrayList<Player> players = new ArrayList<>();

        List<Node> playerFields = playersContainer.getChildren();
        int i;
        for (i = 0; i < playerFields.size(); i++) {
            Pane playerField = (Pane) playerFields.get(i);
            TextInputControl currentTextField = (TextInputControl) playerField.getChildren().get(1);
            String name = currentTextField.getText().trim();
            if (name.isBlank()) {
                valid = false;
                playerField.getStyleClass().add("emptyField");
            } else {
                players.add(new Player(i + 1, name));
                playerField.getStyleClass().removeAll("emptyField");
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
