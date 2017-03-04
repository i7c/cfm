package org.rliz.cfm.common.exception;

/**
 * Thrown if an entity to which the user referred to directly does not exist.
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String s) {
        super(s);
    }

}
