package de.leon.metronom.CustomClasses.CustomExceptions;

public class BpmNotAnIntegerException extends BpmInvalidException {

    public BpmNotAnIntegerException() {
        super();
    }

    public BpmNotAnIntegerException(String message) {
        super(message);
    }

    public BpmNotAnIntegerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BpmNotAnIntegerException(Throwable cause) {
        super(cause);
    }

}
