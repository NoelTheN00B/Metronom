package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class TactNotAnIntegerException extends NotAnIntegerException {

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
