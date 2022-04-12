package sh.shinterface.db;

/**
 * Exception (Runtime) die wordt opgegooid tijdens datatoegang in DAO-model.
 */
public class DataAccessException extends RuntimeException {

    /**
     * Constructor van DataAccessException.
     *
     * @param message   Bericht wat wordt weergegeven.
     * @param exception De 'echte'/oorspronkelijke exception
     */
    public DataAccessException(String message, Throwable exception) {
        super(message, exception);
    }
}
