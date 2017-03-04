package org.rliz.cfm.common.exception;

/**
 * Thrown if the user is authenticated but does not have the required authorization to perform certain actions.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String s) {
        super(s);
    }
}
