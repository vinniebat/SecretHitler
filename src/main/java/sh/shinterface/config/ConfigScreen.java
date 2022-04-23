package sh.shinterface.config;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sh.shinterface.Main;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Configuration screen for selecting the party. Used for putting in player names,
 * selecting the active player that represents the user and if the user is fascist,
 * selecting the players that all are fascist.
 */
public class ConfigScreen extends StackPane implements InvalidationListener {

    /**
     * Minimum amount of players
     */
    private static final int MIN_PLAYERS = 5;

    /**
     * Max amount of players
     */
    private static final int MAX_PLAYERS = 10;

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

    private final GridPane fascistPane = new GridPane();

    /**
     * Button to create a game with the given players
     */
    private final Button createGameButton = new Button("Create Game");

    /**
     * Model that holds the party that is being selected
     */
    private final PartyModel model = new PartyModel();

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
        for (int i = MIN_PLAYERS; i <= MAX_PLAYERS; i++) {
            choiceBox.getItems().add(i);
        }
        choiceBox.setOnAction(e -> model.setPartySize(choiceBox.getValue()));
        // END CHOICE BOX

        // INIT Player fields
        VBox playerContainer = new VBox();
        playerFields = playerContainer.getChildren(); // Sla de fields op voor makkelijk aan te passen
        VBox.setVgrow(playerContainer, Priority.ALWAYS);
        //END PLAYER FIELDS

        // INIT Role Selection
        Label label = new Label("Your role:");
        HBox hBox = new HBox(label, roleBox);
        roleBox.setOnAction(e -> model.setActiveRole(roleBox.getValue()));
        hBox.getStyleClass().add("role-box");
        HBox.setHgrow(hBox, Priority.ALWAYS); // Fills window width
        fascistPane.getStyleClass().add("roles");
        // END ROLE SELECTION

        // INIT Create Game Button
        createGameButton.setOnAction(Main::confirmSelection);
        // END BUTTON

        VBox controlsBox = new VBox(choiceBox, playerContainer, new HBox(fascistPane), new HBox(hBox, createGameButton));
        controlsBox.getStyleClass().add("config-screen");
        // END CONTROLS

        Label title = new Label("SECRET HITLER"); // Titel van het configuratiescherm
        title.getStyleClass().add("title");

        this.getChildren().addAll(controlsBox, title);
        this.getStyleClass().addAll("liberal");
        boolean maxed = stage.isFullScreen() || stage.isMaximized();
        setStyle("-fx-font-size: " + Screen.getPrimary().getBounds().getWidth() * ((maxed) ? 0.03 : 0.01));

        model.addListener(this);
        choiceBox.setValue(MIN_PLAYERS); // Trigger het model met het minimum aantal spelers
        getStylesheets().add("sh/shinterface/stylesheets/configscreen.css");
    }

    @Override
    public void invalidated(Observable observable) {
        setPlayerFields();
        setRoleBox();
        setFascistBoxes();
        setCreateDisable();
        setStyling();
        if (!stage.isMaximized() && !stage.isFullScreen())
            stage.sizeToScene();
    }

    /**
     * Add/Remove PlayerFields according to the party size
     */
    private void setPlayerFields() {
        int size = model.getPartySize();

        if (size == playerFields.size())
            return;

        if (size > playerFields.size()) {
            List<Player> party = model.getParty();
            while (playerFields.size() < size) {
                playerFields.add(
                        new PlayerField(party.get(playerFields.size()), activePlayerGroup, model)
                );
            }
        } else {
            playerFields.retainAll(new ArrayList<>(playerFields.subList(0, size)));
            activePlayerGroup.getToggles().retainAll(new ArrayList<>(activePlayerGroup.getToggles().subList(0, size)));
        }
        for (Node node : playerFields) {
            ((PlayerField) node).reset();
        }
    }

    /**
     * Changes the roleBox visibility according to the chosen active player
     */
    private void setRoleBox() {
        roleBox.getParent().setVisible(model.getActivePlayer().isPresent());
        if (model.getActivePlayer().isEmpty()) {
            roleBox.setValue(Role.NONE);
        }
    }

    /**
     * Adds/Removes player role boxes according to party size and active player role
     */
    private void setFascistBoxes() {
        List<Node> fascistBoxes = fascistPane.getChildren();

        if (!model.knowsFascist()) {
            for (Node node : fascistBoxes) {
                model.removeListener((PlayerRoleBox) node);
            }
            fascistBoxes.clear();
        } else if (fascistBoxes.size() < model.maxFascistCount()) {
            while (fascistBoxes.size() < model.maxFascistCount()) {
                fascistPane.add(new PlayerRoleBox(fascistBoxes.size(), model), fascistBoxes.size() % 2, fascistBoxes.size() / 2);
            }
        } else {
            fascistBoxes.retainAll(new ArrayList<>(fascistBoxes.subList(0, model.maxFascistCount())));
        }
    }

    /**
     * Disables/Enables the create game button according to if the current party state is valid
     */
    private void setCreateDisable() {
        createGameButton.setDisable(!model.isValid());
    }

    /**
     * Sets styling according to active player role
     */
    private void setStyling() {
        this.getStyleClass().removeAll("liberal", "fascist");
        Optional<Player> activePlayer = model.getActivePlayer();
        getStyleClass().add((activePlayer.isPresent() ? activePlayer.get().getRole().getStyle() : "liberal"));
    }

    /**
     * Returns the selected party
     *
     * @return The party as configured in this screen
     */
    public List<Player> getPlayers() {
        return model.getFinalParty();
    }

    /**
     * Check if every player name has been entered
     *
     * @return True if all player names where entered
     */
    public boolean isValid() {
        boolean valid = true;
        for (Node node : playerFields) {
            valid &= ((PlayerField) node).isValid();
        }
        for (Node node : fascistPane.getChildren()) {
            valid &= ((PlayerRoleBox) node).isValid();
        }
        return valid;
    }

    /**
     * Returns the active player, if chosen
     *
     * @return An Optional holding the active player, or empty if no active player was selected
     */
    public Player getActivePlayer() {
        return (model.getActivePlayer().isPresent()) ? model.getActivePlayer().get() : null;
    }
}
