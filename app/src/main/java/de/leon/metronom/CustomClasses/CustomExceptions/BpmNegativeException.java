package de.leon.metronom.CustomClasses.CustomExceptions;

public class BpmNegativeException extends BpmInvalidException {

    public BpmNegativeException() {
        super();
    }

    public BpmNegativeException(String message) {
        super(message);
    }

    public BpmNegativeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BpmNegativeException(Throwable cause) {
        super(cause);
    }

}
