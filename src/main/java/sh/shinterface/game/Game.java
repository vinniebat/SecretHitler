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
import sh.shinterface.game.component.GameWindow;

import java.util.List;

public class Game {

    private final List<Player> players;
    private final Pane pane;
    private final GameWindow gameWindow;

    public Game(List<Player> players, Role role) {
        this.players = players;
        int i = 0;
        while (i < players.size() && players.get(i).getRole() == Role.UNKNOWN) {
            i++;
        }
        gameWindow = new GameWindow(this, role);
        pane = new VBox(gameWindow);
        VBox.setVgrow(gameWindow, Priority.ALWAYS);
        pane.getStyleClass().addAll(role.getStyle(), "interface");
    }

    public void start(Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("sh/shinterface/interface.css");
        stage.setScene(scene);
        stage.show();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public TableView<Gov> getGovTable() {
        return gameWindow.getGovTable();
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void end() {
        gameWindow.getCreateGovPane().setDisable(true);
    }
}
