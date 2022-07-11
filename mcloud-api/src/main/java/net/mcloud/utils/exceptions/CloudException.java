package net.mcloud.utils.exceptions;

public class CloudException extends RuntimeException {
    private final Throwable cause;

    public CloudException(String message) {
        super(message);
        this.cause = null;
    }

    public CloudException(Throwable cause, String message) {
        super(message);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
