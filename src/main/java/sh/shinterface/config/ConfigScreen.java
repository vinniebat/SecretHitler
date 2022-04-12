package sh.shinterface.config;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

    private final Pane roleContainer = new HBox();

    /**
     * Button to create a game with the given players
     */
    private final Button createGameButton = new Button("Create Game");

    private final List<ChoiceBox<Player>> playerBoxes = new ArrayList<>();

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
        activePlayerGroup.selectedToggleProperty().addListener(this::resetRoleChoice);
        // Zorgt dat de role choice wordt getoond na selecteren van actieve speler
        //END PLAYER FIELDS

        // INIT Role Selection
        HBox hBox = new HBox(new Label("Your role:"), roleBox);
        choiceBox.fireEvent(new ActionEvent()); // Roept meteen updatePlayers op
        hBox.setVisible(false); // Eerst niet zichtbaar
        hBox.getStyleClass().add("role-box");
        HBox.setHgrow(hBox, Priority.ALWAYS); // Fills window width
        roleBox.valueProperty().addListener(this::resetRoleChoice);
        roleBox.setValue(Role.NONE);
        roleContainer.getStyleClass().add("role-box");
        // END ROLE SELECTION

        // INIT Create Game Button
        createGameButton.setOnAction(Main::confirmSelection);
        // END BUTTON

        VBox controlsBox = new VBox(choiceBox, playerContainer, new HBox(hBox, createGameButton), roleContainer);
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
                PlayerField playerField = new PlayerField(i, this);
                playerFields.add(playerField);
                activePlayerGroup.getToggles().add(playerField.getButton());
            }
        } else {
            activePlayerGroup.getToggles().remove(amount, playerFields.size());
            playerFields.remove(amount, playerFields.size()); // Hou enkel het juiste aantal spelers over
        }
        resetRoleChoice(null);
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
            Role role = Role.UNKNOWN;
            players.add((playerField.isEmpty()) ? null :
                    new Player(
                    playerField.getPlayerId(),
                    playerField.getName(),
                    role
            ));
        }
        return players;
    }

    /**
     * Shows/Hides the role selection box according to the selected active player
     *
     * @param observable Unused
     */
    public void resetRoleChoice(Observable observable) {
        ObservableList<Node> roleBoxes = roleContainer.getChildren();
        List<Player> players = getPlayers();
        roleBoxes.clear();
        if (activePlayerGroup.getSelectedToggle() == null) {
            roleBox.getParent().setVisible(false);
            roleBox.setValue(Role.NONE);
        } else {
            roleBox.getParent().setVisible(true);
            PlayerField playerField = ((PlayerField) ((ToggleButton) activePlayerGroup.getSelectedToggle()).getParent());
            if (playerField.isValid() && roleBox.getValue().isFascist()) {
                Player activePlayer = players.stream().filter(p -> p.getRole() != Role.UNKNOWN).findFirst().get();
                players.remove(activePlayer);
                List<ChoiceBox<Player>> choiceBoxes = new ArrayList<>();
                for (int i = 0; i < (playerFields.size() - 1) / 2; i++) {
                    ChoiceBox<Player> playerBox = new ChoiceBox<>(
                            FXCollections.observableArrayList(players)
                    );
                    roleBoxes.add(
                            new VBox(new Label((roleBoxes.size() == 0) ? "Hitler: " : "Fascist: "), playerBox)
                    );
                    choiceBoxes.add(playerBox);
                }
                choiceBoxes.get((roleBox.getValue() == Role.HITLER) ? 0 : 1).setValue(activePlayer);
                choiceBoxes.get((roleBox.getValue() == Role.HITLER) ? 0 : 1).setDisable(true);
            }
        }
        updateStyling();
        stage.sizeToScene();
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

    public boolean isValid() {
        boolean valid = true;
        for (Node node : playerFields) {
            PlayerField playerField = (PlayerField) node;
            valid &= playerField.isValid();
        }
        for (Node node : roleContainer.getChildren()) {
            ChoiceBox<Player> playerChoiceBox = (ChoiceBox<Player>) ((Pane) node).getChildren().get(1);
            valid &= playerChoiceBox.getValue() != null;
        }
        return valid;
    }
}
