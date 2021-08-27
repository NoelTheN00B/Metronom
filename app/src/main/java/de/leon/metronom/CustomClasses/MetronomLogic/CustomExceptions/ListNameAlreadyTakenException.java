package de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions;

public class ListNameAlreadyTakenException extends ListException {

    public ListNameAlreadyTakenException() {
        super();
    }

    public ListNameAlreadyTakenException(String message) {
        super(message);
    }

    public ListNameAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListNameAlreadyTakenException(Throwable cause) {
        super(cause);
    }
}
