package com.domus.gradereport.exception;
import com.domus.gradereport.dto.response.ErrorResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .timestamp(java.time.LocalDateTime.now())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(message)
                        .path(req.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException ex, HttpServletRequest req) {
        log.error(ex.getMessage(), ex);
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> badRequest(IllegalArgumentException ex, HttpServletRequest req) {
        log.error(ex.getMessage(), ex);
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> dateTimeParse(DateTimeParseException ex, HttpServletRequest req) {
        log.error(ex.getMessage(), ex);
        return buildError(HttpStatus.BAD_REQUEST, "Invalid date format", req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generic(Exception ex, HttpServletRequest req) {
        log.error(ex.getMessage(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred: " + ex.getMessage(), req);
    }
}

