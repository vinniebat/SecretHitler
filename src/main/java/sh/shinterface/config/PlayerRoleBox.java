package sh.shinterface.config;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import sh.shinterface.datacontainer.Player;

import java.util.Optional;

public class PlayerRoleBox extends HBox implements InvalidationListener {

    private final ChoiceBox<Player> playerBox;

    private final PartyModel model;

    private final int index;

    public PlayerRoleBox(int index, PartyModel model) {
        this.index = index;
        this.model = model;
        playerBox = new ChoiceBox<>(FXCollections.observableArrayList(model.getUnknownPlayers()));
        playerBox.getSelectionModel().selectedItemProperty().addListener(o -> model.setFascist(playerBox.getSelectionModel().getSelectedItem(), index));
        HBox.setHgrow(playerBox, Priority.ALWAYS);
        GridPane.setHgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(new Label((index == 0) ? "Hitler: " : "Fascist: "), playerBox);
    }

    public boolean isValid() {
        return playerBox.getValue() != null;
    }

    @Override
    public void invalidated(Observable observable) {
        playerBox.getItems().setAll(model.getUnknownPlayers());
        playerBox.setValue(model.getFascist(index));
        Optional<Player> activePlayer = model.getActivePlayer();
        playerBox.setDisable(activePlayer.isPresent() && activePlayer.get().equals(playerBox.getValue()));
    }
}
