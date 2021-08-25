package de.leon.metronom.CustomClasses.CustomExceptions;

public class TactInvalidException extends MetronomException {

    public TactInvalidException() {
        super();
    }

    public TactInvalidException(String message) {
        super(message);
    }

    public TactInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TactInvalidException(Throwable cause) {
        super(cause);
    }

}
