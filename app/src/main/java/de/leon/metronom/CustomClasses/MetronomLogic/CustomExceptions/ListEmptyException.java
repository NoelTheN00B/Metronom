package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class ListEmptyException extends ListException {

    public ListEmptyException() {
        super();
    }

    public ListEmptyException(String message) {
        super(message);
    }

    public ListEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListEmptyException(Throwable cause) {
        super(cause);
    }
}
