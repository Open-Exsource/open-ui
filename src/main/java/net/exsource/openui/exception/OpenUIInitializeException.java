package net.exsource.openui.exception;

public class OpenUIInitializeException extends RuntimeException {

    private final String message;

    public OpenUIInitializeException(final String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
