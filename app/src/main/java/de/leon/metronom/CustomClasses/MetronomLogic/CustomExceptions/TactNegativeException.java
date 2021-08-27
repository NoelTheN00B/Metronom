package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class TactNegativeException extends TactInvalidException {

    public TactNegativeException() {
        super();
    }

    public TactNegativeException(String message) {
        super(message);
    }

    public TactNegativeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TactNegativeException(Throwable cause) {
        super(cause);
    }

}
