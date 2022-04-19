package sh.shinterface.config;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

import java.util.Optional;

/**
 * Wrapper class for a ChoiceBox of Players. Used to select fascist or hitler players
 */
public class PlayerRoleBox extends VBox implements InvalidationListener {

    /**
     * ChoiceBox to select the player for the given role
     */
    private final ChoiceBox<Player> playerBox;

    /**
     * Party from which the players are selected
     */
    private final PartyModel model;

    /**
     * Index of the fascist role in the fascist party
     */
    private final int index;

    /**
     * Makes a PlayerRoleBox with the given index in the fascist party and using the given model
     * @param index Index of the role in the fascist party
     * @param model Party to which this role belongs
     */
    public PlayerRoleBox(int index, PartyModel model) {
        this.index = index;
        this.model = model;
        playerBox = new ChoiceBox<>(FXCollections.observableArrayList(model.getUnknownPlayers()));
        playerBox.getSelectionModel().selectedIndexProperty().addListener(o -> model.setFascist(playerBox.getSelectionModel().getSelectedItem(), index));
        Label label = new Label((index == 0) ? "Hitler: " : "Fascist: ");
        VBox.setVgrow(playerBox, Priority.SOMETIMES);
        HBox.setHgrow(label, Priority.ALWAYS);
        GridPane.setHgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(label, playerBox);
        model.addListener(this);
    }

    public boolean isValid() {
        return playerBox.getValue() != null;
    }

    @Override
    public void invalidated(Observable observable) {
        playerBox.getItems().setAll(model.getUnknownPlayers());
        Player fascist = model.getFascist(index);
        playerBox.setValue(null);
        playerBox.setValue(fascist);
        Optional<Player> activePlayer = model.getActivePlayer();
        playerBox.setDisable(activePlayer.isPresent() && activePlayer.get().equals(playerBox.getValue()));
    }
}
