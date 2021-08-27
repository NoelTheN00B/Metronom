package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class NotAnIntegerException extends MetronomException {

    public NotAnIntegerException() {
        super();
    }

    public NotAnIntegerException(String message) {
        super(message);
    }

    public NotAnIntegerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAnIntegerException(Throwable cause) {
        super(cause);
    }

}
