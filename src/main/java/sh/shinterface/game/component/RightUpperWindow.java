package sh.shinterface.game.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Vote;
import sh.shinterface.game.Game;

import java.util.ArrayList;
import java.util.List;

public class RightUpperWindow extends VBox implements InvalidationListener {

    private final Game game;
    private final TableView<Gov> tableView;

    /**
     * All PlayerComponents controlled by this window
     */
    private final List<PlayerView> players = new ArrayList<>();

    public RightUpperWindow(Game game, TableView<Gov> tableView) {
        this.game = game;
        //contains playerOverview and deck info
        //TODO deck info
        this.tableView = tableView;

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

    @Override
    public void invalidated(Observable observable) {
        Gov gov = tableView.getSelectionModel().getSelectedItem();
        List<Vote> votes = gov.getVotes();
        if (votes == null) {
            for (PlayerView playerView :
                    players) {
                playerView.setLabelGraphic(null);
            }
        } else {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).setLabelGraphic(new Rectangle(15, 20, votes.get(i).getColor()));
            }
        }
    }
}
