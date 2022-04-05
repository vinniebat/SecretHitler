package sh.shinterface.game.component;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.game.Game;

import java.util.HashSet;
import java.util.Set;

public class RightUpperWindow extends HBox {

    private final Game game;
    /**
     * All PlayerComponents controlled by this window
     */
    private final Set<PlayerOverviewComponent> players = new HashSet<>();

    public RightUpperWindow(Game game) {
        this.game = game;
        //contains playerOverview and deck info
        //TODO deck info
        HBox hBox = new HBox();

        HBox playerOverview = new HBox();
        for (Player player : game.getPlayers()) {
            PlayerOverviewComponent component = new PlayerOverviewComponent(player, game, this);
            playerOverview.getChildren().add(component);
            players.add(component);
        }

        this.getChildren().add(playerOverview);
    }

    /**
     * Updates the player to reflect the chosen role
     *
     * @param event Event coming from a role being chosen for a player
     */
    public void updateRoles(ActionEvent event) {
        ChoiceBox<Role> roleBox = (ChoiceBox<Role>) event.getSource();
        Role role = roleBox.getValue();
        PlayerOverviewComponent parent = (PlayerOverviewComponent) roleBox.getParent();
        parent.setRole(role);
        for (PlayerOverviewComponent comp : players) {
            comp.updateBox(this);
        }
    }

    /**
     * Checks if the max amount of fascists has been reached
     *
     * @return True if the max amount of fascist roles has been reached
     */
    public boolean maxFascists() {
        return players.parallelStream().filter(PlayerOverviewComponent::isFascist).count() >= (game.getPlayers().size() - 1) / 2;
    }

    /**
     * Checks if a player has the Hitler role
     *
     * @return True if a player has the Hitler role
     */
    public boolean hasHitler() {
        return players.parallelStream().anyMatch(PlayerOverviewComponent::isHitler);
    }
}
