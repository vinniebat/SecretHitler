package sh.shinterface.control;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import sh.shinterface.model.PartyModel;
import sh.shinterface.playable.Player;

import java.io.IOException;

/**
 * Component that contains the player label and the TextField to input the player name
 */
public class PlayerField extends HBox {

    /**
     * TextField used to input the player name
     */
    @FXML
    private TextField nameField;

    /**
     * Button to indicate the active player
     */
    @FXML
    private ToggleButton button;

    /**
     * party from which the player comes
     */
    private final PartyModel model;

    /**
     * Player controlled by this PlayerField
     */
    private final Player player;

    @FXML
    private Label idLabel;

    public void initialize() {
        nameField.setText(player.getName());
        nameField.textProperty().addListener(e -> setName());
        int playerId = player.getId();
        String idString = (playerId < 10) ? "0" + playerId : "" + playerId;
        idLabel.setText("Player " + idString + ":");
    }


    /**
     * Makes a new PlayerField backed by the given player
     *
     * @param player Player that this PlayerField represents
     * @param group  Group to which the button is added
     * @param model  Party from which the player comes
     */
    public PlayerField(Player player, ToggleGroup group, PartyModel model) {
        this.player = player;
        this.model = model;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playerField.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Could not load playerField.fxml");
            Platform.exit();
        }
        button.setToggleGroup(group);
    }

    /*
    public PlayerField(Player player, ToggleGroup group, PartyModel model) {
        this.player = player;
        this.model = model;
        nameField.setText(player.getName());
        nameField.textProperty().addListener(e -> setName());
        nameField.setPromptText("Enter player name");
        nameField.setOnMouseClicked(e -> getStyleClass().removeAll("empty-field"));
        button.setToggleGroup(group);
        button.setOnAction(this::setActivePlayer);
        int playerId = player.getId();
        String idString = (playerId < 10) ? "0" + playerId : "" + playerId;
        getChildren().addAll(
                new Label("Player " + idString + ":"),
                nameField,
                button
        );
        HBox.setHgrow(nameField, Priority.ALWAYS);
    }
     */

    /**
     * Sets the active player as the player that this field represents
     *
     * @param event Unused
     */
    public void setActivePlayer(Event event) {
        model.setActivePlayer((button.isSelected()) ? player : null);
    }

    /**
     * Return the name entered into the TextField, trimmed.
     *
     * @return Trimmed name that was entered
     */
    public String getName() {
        return nameField.getText().strip();
    }

    /**
     * Sets the name of the internal player to the one entered into the TextField
     */
    public void setName() {
        player.setName(getName());
        model.invalidate();
    }

    /**
     * Returns the player representing this PlayerField
     *
     * @return Player with given name
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Clear the name field and remove the error appearance
     */
    public void reset(MouseEvent event) {
        getStyleClass().removeAll("empty-field"); // Reset de error
    }

    /**
     * Check if the TextField isn't blank
     */
    public boolean isValid() {
        if (!nameField.getText().isBlank()) {
            getStyleClass().removeAll("empty-field");
            return true;
        } else {
            getStyleClass().add("empty-field");
            return false;
        }
    }
}
