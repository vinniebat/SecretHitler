package sh.shinterface.screen;

import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sh.shinterface.playable.gov.Gov;
import sh.shinterface.playable.Player;
import sh.shinterface.playable.Role;

import java.util.ArrayList;
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

    public List<Player> getAvailablePresidents() {
        List<Player> pres = new ArrayList<>();
        int pId = gameWindow.getLastId();
        for (int i = 0; i < 3; i++) {
            pres.add(players.get((i+pId) % players.size()));
        }
        pres.removeIf(Player::isTurnLocked);
        return pres;
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
