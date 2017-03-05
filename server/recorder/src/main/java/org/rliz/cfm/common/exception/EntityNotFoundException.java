package org.rliz.cfm.common.exception;

/**
 * Thrown if an entity is not found, to which the user referred directly (by UUID e.g.).
 */
public class EntityNotFoundException extends ServiceException {

    public static final String EC_UNKNOWN_IDENTIFIER = "rec.entity.unknown_identifier";

    public EntityNotFoundException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
