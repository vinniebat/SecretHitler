package sh.shinterface;

import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class FivePlayerGame extends Bord1Game {

    private final List<Player> players;
    private FivePlayerGameWindow gameVenster;
    private Player activePlayer;
    private final Pane pane;

    public FivePlayerGame(List<Player> players, Role role, Player activePlayer) {
        this.players = players;
        this.activePlayer = activePlayer;
        gameVenster = new FivePlayerGameWindow(this);
        pane = new VBox(gameVenster);
        VBox.setVgrow(gameVenster, Priority.ALWAYS);
        pane.getStyleClass().addAll(role.getStyle(), "interface");
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.sizeToScene();
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
