package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class ListEndReachedException extends ListException {

    public ListEndReachedException() {
        super();
    }

    public ListEndReachedException(String message) {
        super(message);
    }

    public ListEndReachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListEndReachedException(Throwable cause) {
        super(cause);
    }
}
