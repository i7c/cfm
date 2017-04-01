package org.rliz.mbs.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Handle exceptions globally.
 */
@ControllerAdvice
public class MbsExceptionHandler {

    /**
     * Handle {@link MbEntityNotFoundException}s thrown whenever an entity is to be retrieved directly (via uuid) and
     * could not be found.
     *
     * @param ex the exception
     * @return the resource passed to the client
     */
    @ExceptionHandler({MbEntityNotFoundException.class})
    public ResponseEntity<ErrorDto> entityNotFoundExceptions(MbEntityNotFoundException ex) {
        return new ResponseEntity<>(ErrorDto.fromException(ex), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle {@link MbLookupException}s thrown if a release (group) or recording could not be found or identified
     * with the provided information.
     *
     * @param ex the exception
     * @return the resource passed to the client
     */
    @ExceptionHandler({MbLookupException.class})
    public ResponseEntity<ErrorDto> lookupExceptions(MbLookupException ex) {
        return new ResponseEntity<>(ErrorDto.fromException(ex), HttpStatus.NOT_FOUND);
    }

}
