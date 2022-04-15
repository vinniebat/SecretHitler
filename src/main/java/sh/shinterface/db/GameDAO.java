package sh.shinterface.db;

import sh.shinterface.db.datatypes.GameDB;

import java.sql.SQLException;

public interface GameDAO {

    void createTables() throws SQLException;

    void fillTables(GameDB gameDB) throws SQLException;
}
