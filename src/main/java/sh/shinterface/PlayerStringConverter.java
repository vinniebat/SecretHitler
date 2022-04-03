package sh.shinterface;

import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class PlayerStringConverter extends StringConverter<Player> {

    private final Game game;

    public PlayerStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(Player player) {
        if (player != null) {
            return player.getId() + ". " + player.getName();
        }
        return null;
    }

    @Override
    public Player fromString(String s) {
        List<Player> players = game.getPlayers();
        try {
            return players.get(Integer.parseInt(s.substring(0, 1)));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("*ERROR* " + e);
            return null;
        }
    }
}
