package sh.shinterface.game.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.game.Game;
import sh.shinterface.util.ImagePicker;
import sh.shinterface.util.PlayerStringConverter;

public class PlayerView extends VBox {

    /**
     * ChoiceBox to choose a role
     */
    private final ChoiceBox<Role> roleBox = new ChoiceBox<>(
            FXCollections.observableArrayList(Role.UNKNOWN, Role.LIBERAL, Role.FASCIST, Role.HITLER)
    );
    /**
     * Image representing the chosen role (currentRole)
     */
    private final ImageView roleImage;
    /**
     * Current role of this player. Does not equal the chosen in role in roleBox
     */
    private Role currentRole = Role.UNKNOWN;

    /**
     * Makes an overview of a player, consisting of a role-card (image), a label and a ChoiceBox to choose the role
     *
     * @param player the player for which this overview is made
     * @param game   game that this player is a part of
     * @param parent the parent that holds this component
     */
    public PlayerView(Player player, Game game, RightUpperWindow parent) {
        roleImage = new ImageView(ImagePicker.pick(player.getSuspectedFaction()));
        Label playerLabel = new Label(new PlayerStringConverter(game).toString(player));
        roleBox.setValue(Role.UNKNOWN);
        roleBox.setOnAction(parent::updateRoles);

        try {
            if (game.getActivePlayer().equals(player)) {
                roleBox.setDisable(true);
            }
        } catch (NullPointerException e) {
            System.err.println("No active player");
        }
        this.getChildren().addAll(roleImage, playerLabel, roleBox);
    }

    /**
     * Current role of the player
     *
     * @return Current role showing
     */
    public Role getCurrentRole() {
        return currentRole;
    }

    /**
     * Sets the image representing the role and selects that role in the ChoiceBox
     *
     * @param role new Role
     */
    public void setRole(Role role) {
        if (currentRole != role) {
            this.currentRole = role;
            roleBox.getSelectionModel().select(role);
            roleImage.setImage(ImagePicker.pick(role));
        }
    }

    /**
     * Is the current role fascist
     *
     * @return True if Role is Hitler or Fascist
     */
    public boolean isFascist() {
        return currentRole.isFascist();
    }

    /**
     * Is the current role Hitler
     *
     * @return True if Role is Hitler
     */
    public boolean isHitler() {
        return currentRole == Role.HITLER;
    }

    /**
     * Updates the ChoiceBox items according to available roles in the window
     *
     * @param window window that controls this player
     */
    public void updateBox(RightUpperWindow window) {
        ObservableList<Role> roles = roleBox.getItems();
        if (!roles.contains(Role.HITLER)) {
            roles.add(Role.HITLER);
        }
        if (!roles.contains(Role.FASCIST)) {
            roles.add(Role.FASCIST);
        }
        if (window.maxFascists() && !currentRole.isFascist()) {
            roles.removeAll(Role.HITLER, Role.FASCIST);
        } else if (window.hasHitler() && currentRole != Role.HITLER) {
            roles.removeAll(Role.HITLER);
        }
    }
}
