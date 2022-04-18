package sh.shinterface.config;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Player;

/**
 * Component that contains the player label and the TextField to input the player name
 */
public class PlayerField extends HBox {

    /**
     * TextField used to input the player name
     */
    private final TextField nameField = new TextField("TEST");

    /**
     * Button to indicate the active player
     */
    private final ToggleButton button = new ToggleButton("ME!");

    /**
     * party from which the player comes
     */
    private final PartyModel model;

    /**
     * Player controlled by this PlayerField
     */
    private final Player player;

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
        nameField.setText(player.getName());
        nameField.textProperty().addListener(e -> setName());
        nameField.setPromptText("Enter player name");
        button.setToggleGroup(group);
        button.setOnAction(this::setActivePlayer);
        int playerId = player.getId();
        String idString = (playerId < 10) ? "0" + playerId : "" + playerId;
        getChildren().addAll(
                new Label("Player " + idString + ":"),
                nameField,
                button
        );
        VBox.setVgrow(this, Priority.ALWAYS);
        HBox.setHgrow(nameField, Priority.ALWAYS);
    }

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
    public void reset() {
        getStyleClass().removeAll("emptyField"); // Reset de error
    }

    /**
     * Check if the TextField isn't blank
     */
    public boolean isValid() {
        if (!nameField.getText().isBlank()) {
            getStyleClass().removeAll("emptyField");
            return true;
        } else {
            getStyleClass().add("emptyField");
            return false;
        }
    }
}
