package sh.shinterface.game.component;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import sh.shinterface.game.Game;
import sh.shinterface.util.ImagePicker;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.util.PlayerStringConverter;
import sh.shinterface.datacontainer.Role;

import java.util.Set;

public class PlayerOverviewComponent extends VBox {

    private static final Set<Role> FASCROLES = Set.of(Role.HITLER, Role.FASCIST);

    public PlayerOverviewComponent(Player player, Game game, RightUpperWindow parent) {
        ImageView susImage = new ImageView(ImagePicker.pick(player.getSuspectedFaction()));
        Label playerLabel = new Label(new PlayerStringConverter(game).toString(player));
        ChoiceBox<Role> roleChoiceBox = new ChoiceBox<>(
                FXCollections.observableArrayList(Role.UNKNOWN, Role.LIBERAL, Role.FASCIST, Role.HITLER)
        );
        roleChoiceBox.setValue(Role.UNKNOWN);

        roleChoiceBox.valueProperty().addListener((observableValue, oldRole, newRole) -> {
            int hitler = 0;
            int fasc = 0;

            if (oldRole.equals(Role.HITLER) ^ newRole.equals(Role.HITLER)) {
                if (oldRole.equals(Role.HITLER)) {
                    hitler = -1;
                } else {
                    hitler = 1;
                }
            }

            if (FASCROLES.contains(oldRole) ^ FASCROLES.contains(newRole)) {
                if (FASCROLES.contains(oldRole)) {
                    fasc = -1;
                } else {
                    fasc = 1;
                }
            }

            if (!parent.updateFasc(fasc, hitler, susImage, ImagePicker.pick(newRole))) {
                roleChoiceBox.setValue(oldRole);
            }
        });

        try {
            if (game.getActivePlayer().equals(player)) {
                roleChoiceBox.setDisable(true);
            }
        } catch (NullPointerException e) {
            System.err.println("No active player");
        }
        this.getChildren().addAll(susImage, playerLabel, roleChoiceBox);
    }
}
