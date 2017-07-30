package org.rliz.cfm.common.exception;

public class IllegalActionException extends ServiceException {

    public static final String EC_OPERATION_NOT_PERMITTED ="rec.state.operation_not_permitted";

    public IllegalActionException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
