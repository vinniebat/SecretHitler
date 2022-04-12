package sh.shinterface.db;

import java.io.Closeable;

public interface DataAccessContext extends Closeable {

    GameDAO getGameDao();

    void close() throws DataAccessException;
}
