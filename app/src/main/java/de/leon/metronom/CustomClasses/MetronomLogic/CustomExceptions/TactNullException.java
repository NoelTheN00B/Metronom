package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class TactNullException extends TactInvalidException {

    public TactNullException() {
        super();
    }

    public TactNullException(String message) {
        super(message);
    }

    public TactNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public TactNullException(Throwable cause) {
        super(cause);
    }

}
