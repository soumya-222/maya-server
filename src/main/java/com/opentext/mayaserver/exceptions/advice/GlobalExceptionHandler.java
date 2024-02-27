package com.opentext.mayaserver.exceptions.advice;

import com.opentext.mayaserver.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Rajiv
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConversion(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Invalid Create payload data, Create use case can't be processed {}", ex.getBindingResult().getAllErrors());
        ErrorResponseVO errorResponseVO = ApiException.invalidUseCasePayloadException(ex.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST.toString(), "MethodArgumentNotValid");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseVO);
    }

    @ExceptionHandler(MayaServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> mayaServerException(RuntimeException ex) {
        log.error("Exception reported {}", ex.getMessage());
        ErrorResponseVO errorResponseVO = ApiException.mayaException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Maya Server Exception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseVO);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> exceptionHandle(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MayaUseCaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> useCaseNotFound(RuntimeException ex) {
        log.error("Exception reported Use Case Not found {}", ex.getMessage());
        ErrorResponseVO errorResponseVO = ApiException.recordNotFound(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), "Please create the Use Case before deleting");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseVO);
    }

    @ExceptionHandler(MayaUseCaseConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> useCaseCanNotBeDeleted(RuntimeException ex) {
        log.error("Exception reported Use Case can Not be Deleted {}", ex.getMessage());
        ErrorResponseVO errorResponseVO = ApiException.useCaseCanNotBeDeletedException(ex.getMessage(), HttpStatus.CONFLICT.toString(), "Please wait until Use case execution is finished.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseVO);
    }

    @ExceptionHandler(MayaInvalidUserInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> invalidUserInput(RuntimeException ex) {
        log.error("Exception reported {}", ex.getMessage());
        ErrorResponseVO errorResponseVO = ApiException.mayaException(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), "Invalid User Input Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseVO);
    }
}
