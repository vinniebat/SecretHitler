package sh.shinterface.game;

import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.game.component.GameWindow;

import java.util.List;

public class Game {

    private final List<Player> players;
    private final GameWindow gameWindow;

    public Game(List<Player> players, Player activePlayer) {
        this.players = players;
        int i = 0;
        while (i < players.size() && players.get(i).getRole() == Role.UNKNOWN) {
            i++;
        }
        Role role = (activePlayer == null) ? Role.UNKNOWN : activePlayer.getRole();
        gameWindow = new GameWindow(this, role);
        VBox.setVgrow(gameWindow, Priority.ALWAYS);
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
