package sh.shinterface.db;

import javafx.stage.FileChooser;
import sh.shinterface.game.Game;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

class SQLiteGameDAO implements GameDAO {

    private Connection connection;

    SQLiteGameDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTables() throws SQLException {
        CreateTables.createTables(connection);
    }
}
