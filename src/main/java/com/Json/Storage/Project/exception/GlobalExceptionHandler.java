package com.Json.Storage.Project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> handleDataNotFoundException(DataNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(ex.getMessage(),HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(InvalidQueryParamException.class)
    public ResponseEntity<?> handleInvalidQueryParamException(InvalidQueryParamException ex){
        return ResponseEntity.badRequest().body(errorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Map<String,Object> errorResponse(String message, HttpStatus status) {
        return Map.of("timestamp", LocalDateTime.now().toString(),
                "status",status.value(),
                "message",message,
                "error",status.getReasonPhrase()
                );
    }
}
