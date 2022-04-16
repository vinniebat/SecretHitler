package sh.shinterface.config;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

import java.util.Optional;

public class PlayerRoleBox extends HBox implements InvalidationListener {

    private final Role role;

    private final ChoiceBox<Player> playerBox;

    private final PartyModel model;

    public PlayerRoleBox(boolean hitler, PartyModel model) {
        this.model = model;
        Label label;
        if (hitler) {
            label = new Label("Hitler: ");
            role = Role.HITLER;
        } else {
            label = new Label("Fascist: ");
            role = Role.FASCIST;
        }
        playerBox = new ChoiceBox<>(FXCollections.observableArrayList(model.getUnknownPlayers()));
        playerBox.getSelectionModel().selectedItemProperty().addListener(o -> swapSelectedPlayer());
        HBox.setHgrow(playerBox, Priority.ALWAYS);
        GridPane.setHgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(label, playerBox);
    }

    public void setValue(Player player) {
        playerBox.setValue(null);
        playerBox.setValue(player);
        model.setPlayerRole(player, role);
    }

    public boolean isValid() {
        return playerBox.getValue() != null;
    }

    public void swapSelectedPlayer() {
        Player oldP = playerBox.getValue();
        if (oldP != null) {
            oldP.setRole(Role.UNKNOWN);
        }
        model.setPlayerRole(playerBox.getSelectionModel().getSelectedItem(), role);
    }

    @Override
    public void invalidated(Observable observable) {
        playerBox.getItems().setAll(model.getUnknownPlayers());
        Optional<Player> activePlayer = model.getActivePlayer();
        Player player = playerBox.getValue();
        if (activePlayer.isPresent()) {
            playerBox.setDisable(activePlayer.get().equals(player));
        } else {
            playerBox.setDisable(false);
        }
    }

    public void clear() {
        model.setPlayerRole(playerBox.getValue(), Role.UNKNOWN);
    }
}
