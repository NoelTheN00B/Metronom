package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class ListNameEmptyException extends ListException {

    public ListNameEmptyException() {
        super();
    }

    public ListNameEmptyException(String message) {
        super(message);
    }

    public ListNameEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListNameEmptyException(Throwable cause) {
        super(cause);
    }
}
