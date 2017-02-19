package org.rliz.mbs.common.exception;

/**
 * Wire representation of errors.
 */
public class ErrorDto {

    private String errorCode;

    private String message;

    public ErrorDto(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static <T extends MbServiceException> ErrorDto fromException(T ex) {
        return new ErrorDto(ex.getErrorCode(), ex.getMessage());
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
