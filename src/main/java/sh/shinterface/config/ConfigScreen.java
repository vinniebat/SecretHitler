package sh.shinterface.config;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sh.shinterface.Main;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Configuration screen that handles the selection of amount of players and inputting the player names.
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
        // Zorgt dat de role choice wordt getoond na selecteren van actieve speler
        //END PLAYER FIELDS

        // INIT Role Selection
        HBox hBox = new HBox(new Label("Your role:"), roleBox);
        roleBox.setOnAction(e -> {
            if (model.getActivePlayer().isPresent()) {
                model.setPlayerRole(model.getActivePlayer().get(), roleBox.getValue());
            }
        });
        hBox.getStyleClass().add("role-box");
        HBox.setHgrow(hBox, Priority.ALWAYS); // Fills window width
        fascistPane.getStyleClass().add("roles");
        // END ROLE SELECTION

        // INIT Create Game Button
        createGameButton.setOnAction(Main::confirmSelection);
        // END BUTTON

        VBox controlsBox = new VBox(choiceBox, playerContainer, fascistPane, new HBox(hBox, createGameButton));
        controlsBox.getStyleClass().add("config-screen");
        // END CONTROLS

        Label title = new Label("SECRET HITLER"); // Titel van het configuratiescherm
        title.getStyleClass().add("title");

        this.getChildren().addAll(controlsBox, title);
        this.getStyleClass().addAll("liberal");

        model.addListener(this);
        choiceBox.setValue(MIN_PLAYERS); // Trigger het model met het minimum aantal spelers
    }

    @Override
    public void invalidated(Observable observable) {
        setPlayerFields();
        setRoleBox();
        setFascistBoxes();
        setCreateDisable();
        setStyling();
        stage.sizeToScene();
    }

    private void setPlayerFields() {
        int size = model.getPartySize();
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
            if (activePlayerGroup.getSelectedToggle() == null) {
                model.setActivePlayer(null);
            }
        }
        for (Node node : playerFields) {
            ((PlayerField) node).reset();
        }
    }

    private void setRoleBox() {
        roleBox.getParent().setVisible(model.getActivePlayer().isPresent());
        if (model.getActivePlayer().isEmpty()) {
            roleBox.setValue(Role.NONE);
        }
    }

    private void setFascistBoxes() {
        Optional<Player> activePlayer = model.getActivePlayer();
        List<Node> fascistBoxes = fascistPane.getChildren();
        if (activePlayer.isEmpty() || !activePlayer.get().getRole().isFascist()) {
            for (Node node : fascistBoxes) {
                model.removeListener((PlayerRoleBox) node);
            }
            fascistBoxes.clear();
        } else {
            if (fascistBoxes.size() < model.maxFascistCount()) {
                while (fascistBoxes.size() < model.maxFascistCount()) {
                    PlayerRoleBox playerRoleBox = new PlayerRoleBox(fascistBoxes.size() == 0, model);
                    fascistPane.add(playerRoleBox, fascistBoxes.size() % 2, fascistBoxes.size() / 2);
                    model.addListener(playerRoleBox);
                }
            } else {
                fascistBoxes.retainAll(new ArrayList<>(fascistBoxes.subList(0, model.maxFascistCount())));
            }
            List<Player> fascists = model.getFascists();
            for (int i = 0; i < fascistBoxes.size(); i++) {
                ((PlayerRoleBox) fascistBoxes.get(i)).setValue(fascists.get(i));
            }
            model.setPlayerRole(activePlayer.get(), roleBox.getValue());
        }
    }

    private void setCreateDisable() {
        createGameButton.setDisable(model.getActivePlayer().isPresent() && model.getActivePlayer().get().getRole().isFascist() && model.getFascistCount() < model.maxFascistCount());
    }

    private void setStyling() {
        this.getStyleClass().removeAll("liberal", "fascist");
        Optional<Player> activePlayer = model.getActivePlayer();
        getStyleClass().add((activePlayer.isPresent() ? activePlayer.get().getRole().getStyle() : "liberal"));
    }

    public List<Player> getPlayers() {
        return model.getFinalParty();
    }

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

    public Optional<Player> getActivePlayer() {
        return model.getActivePlayer();
    }
}
