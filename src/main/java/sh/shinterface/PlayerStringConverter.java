package sh.shinterface;

import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class PlayerStringConverter extends StringConverter<Player> {

    private Game game;

    public PlayerStringConverter(Game game) {
        this.game=game;
    }

    @Override
    public String toString(Player player) {
        if (player!=null) {
            return player.getName();
        }
        return null;
    }

    @Override
    public Player fromString(String s) {
        List<Player> players = game.getPlayers();
        for (Player player :
                players) {
            if (player.getName().equals(s)) {
                return player;
            }
        }
        return null;
    }
}
