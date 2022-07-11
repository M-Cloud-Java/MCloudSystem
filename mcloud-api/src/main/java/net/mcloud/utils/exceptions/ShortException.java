package net.mcloud.utils.exceptions;

public class ShortException extends RuntimeException {
    private final Throwable cause;


    public ShortException(Throwable cause) {
        this.cause = cause;
    }

    public ShortException() {
        this.cause = null;
    }

    public ShortException(Throwable cause, String message) {
        super(message);
        this.cause = cause;
    }

    public ShortException(String message) {
        super(message);
        this.cause = null;
    }

    public Throwable getCause() {
        return cause;
    }
}
