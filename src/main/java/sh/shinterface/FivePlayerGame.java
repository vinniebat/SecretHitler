package sh.shinterface;

import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class FivePlayerGame extends Bord1Game {

    private final List<Player> players;
    private FivePlayerGameWindow gameVenster;
    private Player activePlayer;

    public FivePlayerGame(List<Player> players, Player activePlayer) {
        this.players = players;
        this.activePlayer = activePlayer;
    }

    @Override
    public void start() {
        Stage stage = new Stage();
        gameVenster = new FivePlayerGameWindow(this);
        Scene scene = new Scene(gameVenster);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    public TableView<Gov> getGovTable() {
        return gameVenster.getGovTable();
    }

    @Override
    public Player getActivePlayer() {
        return activePlayer;
    }
}
