package sh.shinterface.db;

import java.sql.Connection;

class SQLiteGameDAO implements GameDAO {

    private Connection connection;

    SQLiteGameDAO(Connection connection) {
        this.connection = connection;
    }
}
