package sh.shinterface.config;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import sh.shinterface.datacontainer.Player;

import static java.util.stream.Collectors.joining;

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

    private final PartyModel model;

    private final Player player;

    public PlayerField(Player player, ToggleGroup group, PartyModel model) {
        this.player = player;
        this.model = model;
        nameField.setText(player.getName());
        nameField.textProperty().addListener(e -> setName());
        button.setToggleGroup(group);
        button.setOnAction(this::setActivePlayer);
        int playerId = player.getId();
        String idString = (playerId < 10) ? "0" + playerId : "" + playerId;
        getChildren().addAll(
                new Label("Player " + idString + ":"),
                nameField,
                button
        );
    }

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

    public void setName() {
        player.setName(getName());
        model.invalidate();
    }

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
