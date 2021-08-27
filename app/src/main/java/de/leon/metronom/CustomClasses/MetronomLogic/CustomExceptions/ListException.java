package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class ListException extends MetronomException {

    public ListException() {
        super();
    }

    public ListException(String message) {
        super(message);
    }

    public ListException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListException(Throwable cause) {
        super(cause);
    }
}
