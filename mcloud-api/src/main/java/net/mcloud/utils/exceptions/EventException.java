package net.mcloud.utils.exceptions;

public class EventException extends RuntimeException {
    private final Throwable cause;

    public EventException(Throwable throwable) {
        cause = throwable;
    }

    public EventException() {
        cause = null;
    }

    public EventException(Throwable throwable, String message) {
        super(message);
        cause = throwable;
    }

    public EventException(String message) {
        super(message);
        cause = null;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
