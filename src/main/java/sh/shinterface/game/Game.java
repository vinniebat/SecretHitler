package sh.shinterface.game;

import javafx.scene.control.TableView;
import javafx.stage.Stage;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Player;

import java.util.List;

public interface Game {

    void start(Stage stage);

    List<Player> getPlayers();

    TableView<Gov> getGovTable();
}
