package org.rliz.mbs.common.exception;

/**
 * Thrown if a non-id lookup fails.
 */
public class MbLookupException extends MbServiceException {

    public static final String EC_NO_RESULTS = "mb.lookup.no_result";

    public static final String EC_LOW_RATING = "mb.lookup.low_rating";

    public MbLookupException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
