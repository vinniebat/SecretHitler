package sh.shinterface.db;

public interface DataAccessProvider {

    DataAccessContext getDataAccessContext() throws DataAccessException;
}
