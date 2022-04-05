package sh.shinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class FivePlayerGame extends Bord1Game {

    private final List<Player> players;
    private final FivePlayerGameWindow gameVenster;
    private final Pane pane;

    public FivePlayerGame(List<Player> players, Role role) {
        this.players = players;
        gameVenster = new FivePlayerGameWindow(this);
        pane = new VBox(gameVenster);
        pane.getStyleClass().addAll(role.getStyle(), "interface");
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.sizeToScene();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    public TableView<Gov> getGovTable() {
        return gameVenster.getGovTable();
    }
}
