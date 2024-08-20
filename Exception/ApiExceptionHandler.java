package com.ogn.orange.domain.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({InvalidEntityException.class})
    ResponseEntity<com.ogn.orange.domain.Exception.ExceptionHandler> NotFoundHandle(InvalidEntityException exception){
        com.ogn.orange.domain.Exception.ExceptionHandler exceptionHandler = new com.ogn.orange.domain.Exception.ExceptionHandler(exception.getMessage(), exception.getErrorCode(),exception.getStatus());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionHandler);
    }

}
