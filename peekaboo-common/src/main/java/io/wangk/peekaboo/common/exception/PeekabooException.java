package io.wangk.peekaboo.common.exception;

public class PeekabooException extends RuntimeException {

    public PeekabooException() {
        super();
    }

    public PeekabooException(String message) {
        super(message);
    }

    public PeekabooException(String message, Throwable cause) {
        super(message, cause);
    }

    public PeekabooException(Throwable cause) {
        super(cause);
    }

    protected PeekabooException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}