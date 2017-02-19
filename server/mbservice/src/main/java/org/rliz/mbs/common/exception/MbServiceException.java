package org.rliz.mbs.common.exception;

/**
 * Exception base class.
 */
public abstract class MbServiceException extends RuntimeException {

    private String errorCode;

    public MbServiceException(String errorCode, String message, Object... args) {
        super(String.format(message, args));
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
