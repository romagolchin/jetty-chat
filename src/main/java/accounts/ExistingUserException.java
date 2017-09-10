package accounts;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class ExistingUserException extends IllegalArgumentException {
    public static final String ERROR_FMT = "User with login %s already exists";

    public ExistingUserException() {
    }

    public ExistingUserException(String s) {
        super(s);
    }

    public ExistingUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingUserException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return String.format(ERROR_FMT, super.getMessage());
    }
}
