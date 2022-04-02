package sh.shinterface;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
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
     * Container that holds all the player fields. A player field consists of a Label (index 0) and TextField (index 1).
     */
    private final Pane playersContainer = new VBox();

    /**
     * Creates a new ConfigurationScreen that is shown on the given stage
     * @param stage Stage that displays the ConfigScreen
     */
    public ConfigScreen(Stage stage) {
        this.stage = stage;
        createContent();
    }

    /**
     * creates the content of the ConfigScreen
     */
    private void createContent() {
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>(); // Selectie voor aantal spelers
        // Add the options
        for (int i = minPlayers; i <= maxPlayers; i++) {
            choiceBox.getItems().add(i);
        }
        choiceBox.setValue(minPlayers); // Standaard-waarde is het minimum
        choiceBox.setOnAction(this::updatePlayers); // Na selectie wordt het aantal spelers ge-update
        choiceBox.fireEvent(new ActionEvent()); // Roept meteen updatePlayers op

        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(Main::confirmSelection);

        VBox vBox = new VBox(); // Container van de controls
        vBox.getChildren().addAll(choiceBox, playersContainer, createGameButton);
        Label title = new Label("SECRET HITLER"); // Titel van het configuratiescherm
        title.getStyleClass().add("title");
        vBox.getStyleClass().add("inner-box");
        this.getStyleClass().add("config-screen");
        this.getChildren().addAll(vBox, title);
    }

    /**
     * Updates the display to reflect the change in amount of players.
     * @param e event coming from the ChoiceBox
     */
    private void updatePlayers(ActionEvent e) {
        ObservableList<Node> playerFields = playersContainer.getChildren();
        for (Node playerField : playerFields) {
            // fk u kris ik doe casting zoveel ik wil
            // We weten da de elementen in de playersContainer HBoxen zijn met daarin éérst een Label en dan een TextField
            ((TextInputControl) ((Pane) playerField).getChildren().get(1)).clear(); // We vegen dus gewoon het textField uit
            playerField.getStyleClass().removeAll("emptyField"); // Reset de error
        }
        int amount = ((ChoiceBox<Integer>) e.getSource()).getValue();
        if (amount > playerFields.size()) { // Voeg spelers toe als er te weinig zijn
            for (int i =  1 + playerFields.size(); i <= amount; i++) {
                playerFields.add(
                        new HBox(
                                new Label("Speler " + i + ":"),
                                new TextField()
                        )
                );
            }
        }
        // We kopiëren de lijst omdat anders java begint te huilen
        List<Node> list = new ArrayList<>(playerFields.subList(0, amount));
        playersContainer.getChildren().retainAll(list); // Hou enkel het juiste aantal spelers over
        stage.sizeToScene(); // Resize het scherm zodat de player fields zichtbaar zijn
    }

    /**
     * Checks if all fields where filled in and returns a list of players.
     * @return If the input is valid, returns a list of players. Otherwise, this returns an empty list.
     */
    public List<Player> getPlayers() {
        boolean valid = true;
        List<Player> players = new ArrayList<>();

        List<Node> playerFields = playersContainer.getChildren();
        for (int i = 0; i < playerFields.size(); i++) {
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
        return (valid) ? players : List.of();
    }
}
