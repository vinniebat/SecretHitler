package sh.shinterface.db;

import java.sql.SQLException;

public interface GameDAO {

    void createTables() throws SQLException;
}
