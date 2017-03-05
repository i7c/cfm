package org.rliz.cfm.common.exception;

/**
 * Base class for service exceptions.
 */
public abstract class ServiceException extends RuntimeException {

    private String errorCode;

    public ServiceException(String errorCode, String message, Object... args) {
        super(String.format(message, args));
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}