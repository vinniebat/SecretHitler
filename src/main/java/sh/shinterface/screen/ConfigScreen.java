package sh.shinterface.screen;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sh.shinterface.Main;
import sh.shinterface.control.PlayerField;
import sh.shinterface.control.PlayerRoleBox;
import sh.shinterface.model.PartyModel;
import sh.shinterface.playable.Player;
import sh.shinterface.playable.Role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Configuration screen for selecting the party. Used for putting in player names,
 * selecting the active player that represents the user and if the user is fascist,
 * selecting the players that all are fascist.
 */
public class ConfigScreen extends TitledScreen implements InvalidationListener {

    /**
     * Stage where the ConfigScreen is present
     */
    private Stage stage;

    @FXML
    private VBox playerContainer;

    /**
     * ToggleGroup for choosing the active player
     */
    private final ToggleGroup activePlayerGroup = new ToggleGroup();

    /**
     * Box that holds the role selection
     */
    @FXML
    private ChoiceBox<Role> roleBox;

    @FXML
    private GridPane fascistPane;

    /**
     * Button to create a game with the given players
     */
    @FXML
    private Button createGameButton;

    /**
     * Model that holds the party that is being selected
     */
    private final PartyModel model = new PartyModel();


    public ConfigScreen() {
        super("NEW GAME");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("configScreen.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            System.err.println("Could not load configScreen.fxml");
            Platform.exit();
        }
        model.addListener(this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        boolean maxed = stage.isFullScreen() || stage.isMaximized();
        setStyle("-fx-font-size: " + Screen.getPrimary().getBounds().getWidth() * ((maxed) ? 0.03 : 0.01));
        model.setPartySize(5);
    }

    public void setPartySize(ActionEvent event) {
        ChoiceBox<Integer> choiceBox = (ChoiceBox<Integer>) event.getSource();
        model.setPartySize(choiceBox.getValue());
    }

    public void setActiveRole(ActionEvent event) {
        model.setActiveRole(roleBox.getValue());
    }

    public void confirm(ActionEvent event) {
        Main.confirmSelection();
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
        ObservableList<Node> playerFields = playerContainer.getChildren();
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
        for (Node node : playerContainer.getChildren()) {
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
