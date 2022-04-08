package sh.shinterface.game;

import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.game.component.FivePlayerGameWindow;

import java.util.List;

public class Game {

    private final List<Player> players;
    private final Pane pane;
    private final FivePlayerGameWindow gameVenster;

    public Game(List<Player> players, Role role) {
        this.players = players;
        int i = 0;
        while (i < players.size() && players.get(i).getRole() == Role.UNKNOWN) {
            i++;
        }
        gameVenster = new FivePlayerGameWindow(this, role);
        pane = new VBox(gameVenster);
        VBox.setVgrow(gameVenster, Priority.ALWAYS);
        pane.getStyleClass().addAll(role.getStyle(), "interface");
    }

    public void start(Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public TableView<Gov> getGovTable() {
        return gameVenster.getGovTable();
    }
}
