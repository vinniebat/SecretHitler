package sh.shinterface.db;

public class SQLiteDataAccessProvider implements DataAccessProvider {

    private final String dbName;

    public SQLiteDataAccessProvider(String dbName) {
        this.dbName = "jdbc:sqlite:" + dbName;
    }

    @Override
    public DataAccessContext getDataAccessContext() throws DataAccessException {
        return new SQLiteDataAccessContext(dbName);
    }
}
