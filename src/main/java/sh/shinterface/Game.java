package sh.shinterface;

import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public interface Game {

    void start(Stage stage);

    List<Player> getPlayers();

    TableView<Gov> getGovTable();

    Player getActivePlayer();
}
