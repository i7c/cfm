package org.rliz.cfm.common.exception;

/**
 * Thrown if the user provides semantically invalid arguments.
 */
public class ApiArgumentsInvalidException extends ServiceException {

    public static final String EC_ARGS_INSUFFICIENT = "rec.api.args.insufficient";

    public ApiArgumentsInvalidException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

}
