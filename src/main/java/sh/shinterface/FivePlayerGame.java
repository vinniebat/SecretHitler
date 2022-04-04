package sh.shinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class FivePlayerGame extends Bord1Game {

    private final List<Player> players;
    private FivePlayerGameWindow gameVenster;

    public FivePlayerGame(List<Player> players) {
        this.players = players;
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
}
