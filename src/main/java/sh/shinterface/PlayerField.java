package sh.shinterface;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

/**
 * Component that contains the player label and the TextField to input the player name
 */
public class PlayerField extends HBox {

    /**
     * TextField used to input the player name
     */
    private final TextField nameField = new TextField();

    /**
     * Players ID
     */
    private final int playerId;

    /**
     * Button to indicate the active player
     */
    private final ToggleButton button = new ToggleButton("ME!");

    /**
     * Create a player with given player id
     * @param playerId Number of the player
     */
    public PlayerField(int playerId) {
        getChildren().addAll(
                new Label("Player " + playerId + ":"),
                nameField,
                button
        );
        this.playerId = playerId;
        nameField.setOnAction(Main::confirmSelection);
    }

    /**
     * Return the name entered into the TextField, trimmed.
     * @return Trimmed name that was entered
     */
    public String getName() {
        return nameField.getText().trim();
    }

    /**
     * Clear the name field and remove the error appearance
     */
    public void reset() {
        getStyleClass().removeAll("emptyField"); // Reset de error
    }

    public int getPlayerId() {
        return playerId;
    }

    /**
     * Check if the TextField isn't blank
     */
    public boolean isValid() {
        if (!nameField.getText().isBlank()) {
            getStyleClass().removeAll("emptyField");
            return true;
        } else  {
            getStyleClass().add("emptyField");
            return false;
        }
    }

    /**
     * @return The active player ToggleButton
     */
    public ToggleButton getButton() {
        return button;
    }
}
