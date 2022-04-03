package sh.shinterface;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
     * ToggleGroup for choosing the active player
     */
    private final ToggleGroup activePlayerGroup = new ToggleGroup();

    /**
     * Box that holds the role selection
     */
    private final ChoiceBox<String> roleBox = new ChoiceBox<>(
            FXCollections.observableArrayList("Liberal", "Fascist", "Hitler")
    );

    /**
     * Creates a new ConfigurationScreen that is shown on the given stage
     * @param stage Stage that displays the ConfigScreen
     */
    public ConfigScreen(Stage stage) {
        this.stage = stage;

        // INIT Controls

         // INIT ChoiceBox to select the amount of players
          ChoiceBox<Integer> choiceBox = new ChoiceBox<>(); // Selectie voor aantal spelers
          // Add the options
          for (int i = minPlayers; i <= maxPlayers; i++) {
          choiceBox.getItems().add(i);
          }
          choiceBox.setValue(minPlayers); // Standaard-waarde is het minimum
          choiceBox.setOnAction(this::updatePlayers); // Na selectie wordt het aantal spelers ge-update
         // END CHOICE BOX

         // INIT Player fields
          VBox playerContainer = new VBox();
          playerFields = playerContainer.getChildren(); // Sla de fields op voor makkelijk aan te passen
          choiceBox.fireEvent(new ActionEvent()); // Roept meteen updatePlayers op
          activePlayerGroup.selectedToggleProperty().addListener(this::updateRoleChoice);
          // Zorgt dat de role choice wordt getoond na selecteren van actieve speler
         //END PLAYER FIELDS

         // INIT Role Selection
          HBox hBox = new HBox(new Label("Role:"), roleBox);
          hBox.setVisible(false); // Eerst niet zichtbaar
          HBox.setHgrow(hBox, Priority.ALWAYS); // Fills window width
          hBox.getStyleClass().add("role-box");
         // END ROLE SELECTION

         // INIT Create Game Button
          Button createGameButton = new Button("Create Game");
          createGameButton.setOnAction(Main::confirmSelection);
         // END BUTTON

         VBox controlsBox = new VBox(choiceBox, playerContainer, new HBox(hBox, createGameButton));
         controlsBox.getStyleClass().add("inner-box");
        // END CONTROLS

        Label title = new Label("SECRET HITLER"); // Titel van het configuratiescherm
        title.getStyleClass().add("title");

        this.getStyleClass().addAll("config-screen", "liberal");
        this.getChildren().addAll(controlsBox, title);
    }

    /**
     * Updates the display to reflect the change in amount of players.
     * @param e event coming from the ChoiceBox
     */
    private void updatePlayers(ActionEvent e) {
        for (Node node : playerFields) {
            PlayerField playerField = (PlayerField) node;
            // fk u kris ik doe casting zoveel ik wil
            // We weten da de elementen in de playersContainer PlayerFields
            playerField.reset(); // We resetten dus gewoon het tekstveld
        }
        int amount = ((ChoiceBox<Integer>) e.getSource()).getValue();
        if (amount > playerFields.size()) { // Voeg spelers toe als er te weinig zijn
            for (int i =  1 + playerFields.size(); i <= amount; i++) {
                PlayerField playerField = new PlayerField(i);
                playerFields.add(playerField);
                activePlayerGroup.getToggles().add(playerField.getButton());
            }
        } else {
            activePlayerGroup.getToggles().remove(amount, playerFields.size());
            playerFields.remove(amount, playerFields.size()); // Hou enkel het juiste aantal spelers over
            updateRoleChoice(null);
        }
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

    public void updateRoleChoice(Observable observable) {
        roleBox.getParent().setVisible(activePlayerGroup.getSelectedToggle() != null);
        roleBox.setValue("");
    }
}
