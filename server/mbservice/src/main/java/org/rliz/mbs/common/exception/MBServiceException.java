package org.rliz.mbs.common.exception;

/**
 * Exception base class.
 */
public abstract class MBServiceException extends RuntimeException {

    private String errorCode;

    public MBServiceException(String s, String errorCode) {
        super(s);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
