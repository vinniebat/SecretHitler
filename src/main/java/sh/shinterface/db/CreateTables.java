package sh.shinterface.db;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import sh.shinterface.db.datatypes.GameDB;
import sh.shinterface.db.datatypes.GovDB;
import sh.shinterface.db.datatypes.PlayerDB;
import sh.shinterface.db.datatypes.VoteDB;
import sh.shinterface.playable.Player;
import sh.shinterface.playable.Policy;
import sh.shinterface.playable.gov.Gov;
import sh.shinterface.playable.gov.Vote;
import sh.shinterface.screen.Game;
import sh.shinterface.util.SpecialGovPlayers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CreateTables {

    private static void createPlayerTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE players (
                        player_id INTEGER PRIMARY KEY,
                        name  NOT NULL,
                        role TEXT
                    );
                    """
            );
        }
    }

    private static void createGovTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE govs (
                        gov_id INTEGER PRIMARY KEY,
                        president INTEGER NOT NULL REFERENCES players,
                        chancellor INTEGER REFERENCES players,
                        claim1 TEXT,
                        claim2 TEXT,
                        conf INTEGER
                    );
                    """
            );
        }
    }

    private static void createVoteTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE votes (
                        player_id INTEGER REFERENCES players,
                        gov_id INTEGER REFERENCES govs,
                        vote TEXT NOT NULL,
                        PRIMARY KEY (player_id, gov_id)
                    );
                    """
            );
        }
    }

    public static void createDB(Game game, Button button) {
        button.setDisable(true);
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database files", "*.sqlite", "*.SQLITE"));
        File file = chooser.showSaveDialog(game.getGameWindow().getScene().getWindow());
        try {
            if (!file.createNewFile()) {
                System.out.println(file.delete());
                System.out.println(file.createNewFile());
            }
            String path = file.getPath();
            try (DataAccessContext dataAccessContext = new SQLiteDataAccessProvider(path).getDataAccessContext()) {
                GameDAO gameDao = dataAccessContext.getGameDao();
                gameDao.createTables();
                gameDao.fillTables(CreateTables.fromGame(game));
            }
        } catch (Exception e) {
            //TODO error text
            System.err.println("File error: " + e);
            button.setDisable(false);
        }
    }

    static void createTables(Connection connection) throws SQLException {
        createPlayerTable(connection);
        createGovTable(connection);
        createVoteTable(connection);
    }

    private static GameDB fromGame(Game game) {

        List<Player> players = game.getPlayers();
        ObservableList<Gov> govs = game.getGovTable().getItems();

        List<PlayerDB> playerDBs = new ArrayList<>();
        for (Player specialgovplayer : SpecialGovPlayers.SPECIALGOVPLAYERS) {
            int id = specialgovplayer.getId();
            String name = specialgovplayer.getName();
            String role = specialgovplayer.getRole().toString();
            playerDBs.add(new PlayerDB(id, name, role));
        }
        for (Player player : players) {
            int id = player.getId();
            String name = player.getName();
            String role = player.getRole().toString();
            playerDBs.add(new PlayerDB(id, name, role));
        }

        List<GovDB> govDBs = new ArrayList<>();
        List<VoteDB> voteDBs = new ArrayList<>();
        for (Gov gov : govs) {
            int id = govs.indexOf(gov);
            int presidentId = gov.getPresident().getId();
            int chancellorId = gov.getChancellor().getId();
            String claim1 = gov.getClaim1().stream().map(Policy::toString).collect(Collectors.joining());
            String claim2 = gov.getClaim2().stream().map(Policy::toString).collect(Collectors.joining());
            int conf = gov.isConf() ? 1 : 0;

            govDBs.add(new GovDB(id, presidentId, chancellorId, claim1, claim2, conf));

            List<Vote> votes = gov.getVotes();
            for (int i = 0; i < votes.size(); i++) {
                String vote = votes.get(i).toString();
                int playerId = players.get(i).getId();
                voteDBs.add(new VoteDB(playerId, id, vote));
            }
        }
        return new GameDB(playerDBs, govDBs, voteDBs);
    }
}