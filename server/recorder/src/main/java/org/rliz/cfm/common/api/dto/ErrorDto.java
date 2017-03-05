package org.rliz.cfm.common.api.dto;

import org.rliz.cfm.common.exception.ServiceException;

/**
 * Wire representation of errors.
 */
public class ErrorDto {

    private final String errorCode;

    private final String message;

    private ErrorDto(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Build {@link ErrorDto} from given exception.
     *
     * @param ex  exception
     * @param <T> type of the exception
     * @return
     */
    public static <T extends ServiceException> ErrorDto fromException(T ex) {
        return new ErrorDto(ex.getErrorCode(), ex.getMessage());
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
