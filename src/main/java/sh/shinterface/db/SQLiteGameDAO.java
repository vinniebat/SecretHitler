package sh.shinterface.db;

import sh.shinterface.db.datatypes.GameDB;
import sh.shinterface.db.datatypes.GovDB;
import sh.shinterface.db.datatypes.PlayerDB;
import sh.shinterface.db.datatypes.VoteDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class SQLiteGameDAO implements GameDAO {

    private final Connection connection;

    SQLiteGameDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTables() throws SQLException {
        CreateTables.createTables(connection);
    }

    @Override
    public void fillTables(GameDB gameDB) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO players VALUES (?, ?, ?);"
        )) {
            for (PlayerDB player : gameDB.players()) {
                statement.setInt(1, player.id());
                statement.setString(2, player.name());
                statement.setString(3, player.role());
                statement.addBatch();
            }
            statement.executeBatch();
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO govs VALUES (?, ?, ?, ?, ?, ?);"
        )) {
            for (GovDB gov : gameDB.govs()) {
                statement.setInt(1, gov.id());
                statement.setInt(2, gov.presidentId());
                statement.setInt(3, gov.chancellorId());
                statement.setString(4, gov.claim1());
                statement.setString(5, gov.claim2());
                statement.setInt(6, gov.conf());
                statement.addBatch();
            }
            statement.executeBatch();
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO votes VALUES (?, ?, ?);"
        )) {
            for (VoteDB vote : gameDB.votes()) {
                statement.setInt(1, vote.playerId());
                statement.setInt(2, vote.govId());
                statement.setString(3, vote.vote());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}
