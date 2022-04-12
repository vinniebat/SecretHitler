package sh.shinterface.db;

import java.sql.Connection;
import java.sql.DriverManager;

class SQLiteDataAccessContext implements DataAccessContext {

    private final Connection connection;

    SQLiteDataAccessContext(String dbName) {
        try {
            connection = DriverManager.getConnection(dbName);
        } catch (Exception e) {
            throw new DataAccessException("Data Access Exception", e);
        }
    }

    @Override
    public GameDAO getGameDao() {
        return new SQLiteGameDAO(connection);
    }

    @Override
    public void close() throws DataAccessException {
        try {
            connection.close();
        } catch (Exception e) {
            throw new DataAccessException("Data access exception", e);
        }
    }
}
