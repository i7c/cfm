package org.rliz.cfm.common.exception;

/**
 * Exception thrown whenever third-party API returns unexpected results.
 */
public class ThirdPartyApiException extends RuntimeException {

    private String errorCode;

    /**
     * Constructor.
     *
     * @param s         message of the error in English
     * @param errorCode error code
     */
    public ThirdPartyApiException(String s, String errorCode) {
        super(s);
        this.errorCode = errorCode;
    }
}
