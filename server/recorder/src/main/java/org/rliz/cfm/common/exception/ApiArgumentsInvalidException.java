package org.rliz.cfm.common.exception;

/**
 * Thrown if the user provides semantically invalid arguments.
 */
public class ApiArgumentsInvalidException extends RuntimeException {

    private String errorCode;

    public ApiArgumentsInvalidException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
