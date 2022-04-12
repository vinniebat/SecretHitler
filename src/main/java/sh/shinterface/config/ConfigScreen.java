package sh.shinterface.config;

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
import sh.shinterface.Main;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

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
    private final ChoiceBox<Role> roleBox = new ChoiceBox<>(
            FXCollections.observableArrayList(Role.LIBERAL, Role.FASCIST, Role.HITLER)
    );

    /**
     * Button to create a game with the given players
     */
    private final Button createGameButton = new Button("Create Game");

    /**
     * Creates a new ConfigurationScreen that is shown on the given stage
     *
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
        roleBox.valueProperty().addListener(this::updateRoleChoice);
        roleBox.setValue(Role.NONE);
        // END ROLE SELECTION

        // INIT Create Game Button
        createGameButton.setOnAction(Main::confirmSelection);
        // END BUTTON

        VBox controlsBox = new VBox(choiceBox, playerContainer, new HBox(hBox, createGameButton));
        // END CONTROLS

        Label title = new Label("SECRET HITLER"); // Titel van het configuratiescherm
        title.getStyleClass().add("title");

        this.getChildren().addAll(controlsBox, title);
        this.getStyleClass().addAll("config-screen", "liberal");
    }

    /**
     * Updates the display to reflect the change in amount of players.
     *
     * @param e event coming from the ChoiceBox
     */
    private void updatePlayers(ActionEvent e) {
        int amount = ((ChoiceBox<Integer>) e.getSource()).getValue();
        if (amount > playerFields.size()) { // Voeg spelers toe als er te weinig zijn
            for (int i = 1 + playerFields.size(); i <= amount; i++) {
                PlayerField playerField = new PlayerField(i);
                playerFields.add(playerField);
                activePlayerGroup.getToggles().add(playerField.getButton());
            }
        } else {
            activePlayerGroup.getToggles().remove(amount, playerFields.size());
            playerFields.remove(amount, playerFields.size()); // Hou enkel het juiste aantal spelers over
            updateRoleChoice(null);
        }
        for (Node node : playerFields) {
            PlayerField playerField = (PlayerField) node;
            // fk u kris ik doe casting zoveel ik wil
            // We weten da de elementen in de playersContainer PlayerFields
            playerField.reset(); // We resetten dus gewoon het tekstveld
        }
        stage.sizeToScene(); // Resize het scherm zodat de player fields zichtbaar zijn
    }

    /**
     * Checks if all fields where filled in and returns a list of players.
     *
     * @return If the input is valid, returns a list of players. Otherwise, this returns an empty list.
     */
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (Node node : playerFields) {
            PlayerField playerField = (PlayerField) node;
            if (playerField.isValid()) {
                Role role = Role.UNKNOWN;
                if (playerField.isActive()) {
                    role = roleBox.getValue();
                }
                players.add(new Player(
                        playerField.getPlayerId(),
                        playerField.getName(),
                        role
                ));
            }
        }
        return (players.size() == playerFields.size()) ? players : List.of();
    }

    /**
     * Shows/Hides the role selection box according to the selected active player
     *
     * @param observable Unused
     */
    public void updateRoleChoice(Observable observable) {
        if (activePlayerGroup.getSelectedToggle() == null) {
            roleBox.getParent().setVisible(false);
            roleBox.setValue(Role.NONE);
            createGameButton.setDisable(false);
        } else {
            roleBox.getParent().setVisible(true);
            createGameButton.setDisable(roleBox.getValue() == Role.NONE);
        }
        updateStyling();
    }

    /**
     * Get the value of the role selection box
     *
     * @return Role of the active player
     */
    public Role getRole() {
        return roleBox.getValue();
    }

    /**
     * Updates the styling according to the selected role
     */
    private void updateStyling() {
        this.getStyleClass().removeAll("liberal", "fascist");
        this.getStyleClass().add(roleBox.getValue().getStyle());
    }
}
