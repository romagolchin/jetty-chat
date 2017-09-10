package dbService;

/**
 * Thrown if DB is in illegal state
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class DBException extends RuntimeException {
    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }
}
