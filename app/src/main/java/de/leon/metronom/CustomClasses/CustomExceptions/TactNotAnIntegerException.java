package de.leon.metronom.CustomClasses.CustomExceptions;

public class TactNotAnIntegerException extends TactInvalidException {

    public TactNotAnIntegerException() {
        super();
    }

    public TactNotAnIntegerException(String message) {
        super(message);
    }

    public TactNotAnIntegerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TactNotAnIntegerException(Throwable cause) {
        super(cause);
    }

}
