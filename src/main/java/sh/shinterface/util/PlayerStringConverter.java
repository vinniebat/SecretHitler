package sh.shinterface.util;

import javafx.util.StringConverter;
import sh.shinterface.game.Game;
import sh.shinterface.datacontainer.Player;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

//TODO check for different names before starting game + strip() stuff
public class PlayerStringConverter extends StringConverter<Player> {

    private final Game game;

    public PlayerStringConverter(Game game) {
        this.game = game;
    }

    public static String formatName(String name) {
        return Arrays.stream(name.split("\\s")).filter(str -> !str.isBlank()).map(String::strip).map(String::toLowerCase).map(str -> str.substring(0, 1).toUpperCase() + str.substring(1)).collect(joining(" "));
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