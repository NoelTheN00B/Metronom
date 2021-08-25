package de.leon.metronom.CustomClasses.CustomExceptions;

public class BpmNullException extends BpmInvalidException {

    public BpmNullException() {
        super();
    }

    public BpmNullException(String message) {
        super(message);
    }

    public BpmNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public BpmNullException(Throwable cause) {
        super(cause);
    }

}
