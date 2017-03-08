package org.rliz.cfm.common.exception;

/**
 * Thrown if the user is authenticated but does not have the required authorization to perform certain actions.
 */
public class UnauthorizedException extends ServiceException {

    public static final String EC_PLAYBACK_DELETE = "rec.authorization.playbacks.delete";

    public static final String EC_PLAYBACK_FIX = "rec.authorization.playbacks.fix";

    public UnauthorizedException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
