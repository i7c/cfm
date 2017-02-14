package org.rliz.mbs.common.exception;

/**
 * Thrown when a musicbrainz entity was not found with the given lookup criteria.
 */
public class MbEntityNotFoundException extends MBServiceException {

    public MbEntityNotFoundException(String s, String errorCode) {
        super(s, errorCode);
    }
}
