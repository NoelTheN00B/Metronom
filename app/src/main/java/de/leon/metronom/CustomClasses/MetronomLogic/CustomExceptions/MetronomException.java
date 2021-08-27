package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class MetronomException extends Exception {

    public MetronomException() {
    super();
}

    public MetronomException(String message) {
        super(message);
    }

    public MetronomException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetronomException(Throwable cause) {
        super(cause);
    }
}
