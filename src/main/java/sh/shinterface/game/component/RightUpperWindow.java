package sh.shinterface.game.component;

import javafx.scene.layout.HBox;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.game.Game;

import java.util.HashSet;
import java.util.Set;

public class RightUpperWindow extends HBox {

    private final Game game;
    /**
     * All PlayerComponents controlled by this window
     */
    private final Set<PlayerView> players = new HashSet<>();

    public RightUpperWindow(Game game) {
        this.game = game;
        //contains playerOverview and deck info
        //TODO deck info
        HBox hBox = new HBox();

        HBox playerOverview = new HBox();
        for (Player player : game.getPlayers()) {
            PlayerView component = new PlayerView(player, game, this);
            playerOverview.getChildren().add(component);
            players.add(component);
        }

        this.getChildren().add(playerOverview);
    }

    /**
     * Updates the players to match available roles
     */
    public void updateChoices() {
        for (PlayerView comp : players) {
            comp.updateBox();
        }
    }

    /**
     * Checks if the max amount of fascists has been reached
     *
     * @return True if the max amount of fascist roles has been reached
     */
    public boolean maxFascists() {
        return players.parallelStream().filter(PlayerView::isFascist).count() >= (game.getPlayers().size() - 1) / 2;
    }

    /**
     * Checks if a player has the Hitler role
     *
     * @return True if a player has the Hitler role
     */
    public boolean hasHitler() {
        return players.parallelStream().anyMatch(PlayerView::isHitler);
    }
}
