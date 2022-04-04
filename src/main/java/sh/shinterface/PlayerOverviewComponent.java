package sh.shinterface;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PlayerOverviewComponent extends VBox {

    public PlayerOverviewComponent(Player player, Game game) {
        ImageView susImage = new ImageView(ImagePicker.pick(player.getSuspectedFaction()));
        Label playerLabel = new Label(new PlayerStringConverter(game).toString(player));
        ChoiceBox<Role> roleChoiceBox = new ChoiceBox<>(
                FXCollections.observableArrayList(Role.UNKNOWN, Role.LIBERAL, Role.FASCIST, Role.HITLER)
        );
        roleChoiceBox.setValue(Role.UNKNOWN);
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
