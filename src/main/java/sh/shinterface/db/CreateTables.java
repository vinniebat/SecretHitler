package sh.shinterface.db;

import javafx.stage.FileChooser;
import sh.shinterface.game.Game;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
                        persoon_id INTEGER REFERENCES players,
                        gov_id INTEGER REFERENCES govs,
                        vote TEXT NOT NULL,
                        PRIMARY KEY (persoon_id, gov_id)
                    );
                    """
            );
        }
    }

    public static void createDB(Game game) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database files", "*.sqlite", "*.SQLITE"));
        //TODO juiste window ingeven
        File file = chooser.showSaveDialog(null);
        try {
            if (!file.createNewFile()) {
                if (file.delete()) {
                    file.createNewFile();
                    createTables(file.getPath());
                } else {
                    //TODO error
                }
            } else {
                createTables(file.getPath());
            }
        } catch (Exception e) {
            System.err.println("File error: " + e);
        }
    }

    private static void createTables(String path) throws SQLException {
        try (DataAccessContext dataAccessContext = new SQLiteDataAccessProvider(path).getDataAccessContext()) {
            dataAccessContext.getGameDao().createTables();
        }
    }

    static void createTables(Connection connection) throws SQLException {
        createPlayerTable(connection);
        createGovTable(connection);
        createVoteTable(connection);
    }


}