package sh.shinterface.game.component;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sh.shinterface.game.Game;

import java.util.HashSet;
import java.util.Set;

public class RightUpperWindow extends VBox {

    private final Game game;
    /**
     * All PlayerComponents controlled by this window
     */
    private final Set<PlayerView> players = new HashSet<>();

    public RightUpperWindow(Game game) {
        this.game = game;
        //contains playerOverview and deck info
        //TODO deck info


        HBox playerOverview1 = new HBox();
        HBox playerOverview2 = new HBox();
        for (int i = 0; i < game.getPlayers().size(); i++) {
            PlayerView component = new PlayerView(game.getPlayers().get(i), game, this);
            if (playerOverview1.getChildren().size() == 5) {
                playerOverview2.getChildren().add(component);
            } else {
                playerOverview1.getChildren().add(component);
            }
            players.add(component);
        }

        playerOverview1.getStyleClass().add("centeredHBox");
        playerOverview2.getStyleClass().add("centeredHBox");

        this.getChildren().addAll(playerOverview1, playerOverview2);
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
