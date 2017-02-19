package org.rliz.mbs.common.exception;

/**
 * Thrown when a musicbrainz entity was not found with the given lookup criteria.
 */
public class MbEntityNotFoundException extends MbServiceException {

    public static final String EC_NO_SUCH_UUID = "mb.entity.uuid_not_found";

    public MbEntityNotFoundException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
