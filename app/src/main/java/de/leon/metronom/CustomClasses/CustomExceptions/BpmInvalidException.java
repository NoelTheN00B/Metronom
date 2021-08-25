package de.leon.metronom.CustomClasses.CustomExceptions;

public class BpmInvalidException extends MetronomException {

    public BpmInvalidException() {
        super();
    }

    public BpmInvalidException(String message) {
        super(message);
    }

    public BpmInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public BpmInvalidException(Throwable cause) {
        super(cause);
    }

}
