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

    @ExceptionHandler({MbEntityNotFoundException.class})
    public ResponseEntity<ErrorDto> entityNotFoundExceptions(MbEntityNotFoundException ex) {
        return new ResponseEntity<>(ErrorDto.fromException(ex), HttpStatus.NOT_FOUND);
    }

}
