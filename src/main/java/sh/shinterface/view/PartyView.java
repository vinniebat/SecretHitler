package sh.shinterface.view;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import sh.shinterface.control.PlayerView;
import sh.shinterface.playable.Player;
import sh.shinterface.playable.gov.Gov;
import sh.shinterface.playable.gov.Vote;
import sh.shinterface.screen.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartyView extends VBox implements InvalidationListener {

    private Game game;
    private TableView<Gov> tableView;

    @FXML
    private HBox topPlayerOverview;

    @FXML
    public HBox botPlayerOverview;

    /**
     * All PlayerComponents controlled by this window
     */
    private final List<PlayerView> playerViews = new ArrayList<>();

    public PartyView() {
        //contains playerOverview and deck info
        //TODO deck info
        FXMLLoader loader = new FXMLLoader(getClass().getResource("partyView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            System.err.println("Could not load partyView.fxml");
            Platform.exit();
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        List<Player> players = game.getPlayers();
        int i = 0;
        while (i < 5) {
            PlayerView component = new PlayerView(players.get(i), game, this);
            topPlayerOverview.getChildren().add(component);
            playerViews.add(component);
            i++;
        }
        while (i < players.size()) {
            PlayerView component = new PlayerView(players.get(i), game, this);
            botPlayerOverview.getChildren().add(component);
            playerViews.add(component);
            i++;
        }
        game.getGovTable().getSelectionModel().selectedItemProperty().addListener(this);
    }

    public TableView<Gov> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Gov> tableView) {
        this.tableView = tableView;
    }

    public PartyView(Game game, TableView<Gov> tableView) {
        this.game = game;
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
            playerViews.add(component);
        }

        this.getChildren().addAll(playerOverview1, playerOverview2);
    }

    /**
     * Updates the players to match available roles
     */
    public void updateChoices() {
        for (PlayerView comp : playerViews) {
            comp.updateBox();
        }
    }

    /**
     * Checks if the max amount of fascists has been reached
     *
     * @return True if the max amount of fascist roles has been reached
     */
    public boolean maxFascists() {
        return playerViews.parallelStream().filter(PlayerView::isFascist).count() >= (game.getPlayers().size() - 1) / 2;
    }

    /**
     * Checks if a player has the Hitler role
     *
     * @return True if a player has the Hitler role
     */
    public boolean hasHitler() {
        return playerViews.parallelStream().anyMatch(PlayerView::isHitler);
    }

    @Override
    public void invalidated(Observable observable) {
        Gov gov = tableView.getSelectionModel().getSelectedItem();
        List<Vote> votes = gov.getVotes();
        if (votes == null) {
            for (PlayerView playerView :
                    playerViews) {
                playerView.setLabelGraphic(null);
            }
        } else {
            for (int i = 0; i < playerViews.size(); i++) {
                playerViews.get(i).setLabelGraphic(new Rectangle(15, 20, votes.get(i).getColor()));
            }
        }
    }
}
