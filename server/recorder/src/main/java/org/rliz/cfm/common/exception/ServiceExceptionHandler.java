package org.rliz.cfm.common.exception;

import org.rliz.cfm.common.api.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handle Exceptions globally.
 */
@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorDto> entityNotFoundExceptions(EntityNotFoundException ex) {
        return new ResponseEntity<>(ErrorDto.fromException(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ApiArgumentsInvalidException.class})
    public ResponseEntity<ErrorDto> apiArgumentsInvalidExceptions(ApiArgumentsInvalidException ex) {
        return new ResponseEntity<>(ErrorDto.fromException(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<ErrorDto> unauthorizedExceptions(UnauthorizedException ex) {
        return new ResponseEntity<>(ErrorDto.fromException(ex), HttpStatus.UNAUTHORIZED);
    }

}
