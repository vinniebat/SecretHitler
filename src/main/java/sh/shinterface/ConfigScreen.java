package sh.shinterface;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration screen that handles the selection of amount of players and inputting the player names.
 */
public class ConfigScreen extends StackPane {

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
    private final Stage stage;

    /**
     * The PlayerFields that hold the input fields
     */
    private final ObservableList<Node> playerFields;

    /**
     * Creates a new ConfigurationScreen that is shown on the given stage
     * @param stage Stage that displays the ConfigScreen
     */
    public ConfigScreen(Stage stage) {
        this.stage = stage;
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>(); // Selectie voor aantal spelers
        // Add the options
        for (int i = minPlayers; i <= maxPlayers; i++) {
            choiceBox.getItems().add(i);
        }
        choiceBox.setValue(minPlayers); // Standaard-waarde is het minimum
        choiceBox.setOnAction(this::updatePlayers); // Na selectie wordt het aantal spelers ge-update

        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(Main::confirmSelection);

        VBox controlsBox = new VBox(); // Container van de controls
        VBox playerContainer = new VBox();
        playerFields = playerContainer.getChildren();
        controlsBox.getChildren().addAll(choiceBox, playerContainer, createGameButton);
        Label title = new Label("SECRET HITLER"); // Titel van het configuratiescherm
        title.getStyleClass().add("title");
        controlsBox.getStyleClass().add("inner-box");
        this.getStyleClass().addAll("config-screen", "liberal");
        this.getChildren().addAll(controlsBox, title);

        choiceBox.fireEvent(new ActionEvent()); // Roept meteen updatePlayers op
    }

    /**
     * Updates the display to reflect the change in amount of players.
     * @param e event coming from the ChoiceBox
     */
    private void updatePlayers(ActionEvent e) {
        for (Node node : playerFields) {
            PlayerField playerField = (PlayerField) node;
            // fk u kris ik doe casting zoveel ik wil
            // We weten da de elementen in de playersContainer HBoxen zijn met daarin éérst een Label en dan een TextField
            playerField.reset(); // We vegen dus gewoon het textField uit
        }
        int amount = ((ChoiceBox<Integer>) e.getSource()).getValue();
        if (amount > playerFields.size()) { // Voeg spelers toe als er te weinig zijn
            for (int i =  1 + playerFields.size(); i <= amount; i++) {
                playerFields.add(new PlayerField(i));
            }
        }
        playerFields.remove(amount, playerFields.size()); // Hou enkel het juiste aantal spelers over
        stage.sizeToScene(); // Resize het scherm zodat de player fields zichtbaar zijn
    }

    /**
     * Checks if all fields where filled in and returns a list of players.
     * @return If the input is valid, returns a list of players. Otherwise, this returns an empty list.
     */
    public List<Player> getPlayers() {
        boolean valid = true;
        List<Player> players = new ArrayList<>();
        for (Node node : playerFields) {
            PlayerField playerField = (PlayerField) node;
            valid &= playerField.isValid();
            if (playerField.isValid()) {
                players.add(new Player(
                        playerField.getPlayerId(),
                        playerField.getName()
                ));
            }
        }
        return (valid) ? players : List.of();
    }
}
