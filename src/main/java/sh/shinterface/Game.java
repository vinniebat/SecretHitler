package sh.shinterface;

import javafx.scene.control.TableView;

import java.util.List;

public interface Game {

    void start();

    List<Player> getPlayers();

    TableView<Gov> getGovTable();
}
