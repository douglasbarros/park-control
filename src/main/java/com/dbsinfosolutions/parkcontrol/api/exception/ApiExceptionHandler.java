package com.dbsinfosolutions.parkcontrol.api.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dbsinfosolutions.parkcontrol.domain.exception.BusinessException;
import com.dbsinfosolutions.parkcontrol.domain.exception.GarageFullException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_CONTENT, "Validation Error",
                exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(GarageFullException.class)
    public ResponseEntity<ApiErrorResponse> handleGarageFull(GarageFullException exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Conflict", exception.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", exception.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                exception.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String error,
            String message, String path) {
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(Instant.now(), status.value(), error, message, path));
    }

    public record ApiErrorResponse(Instant timestamp, int status, String error, String message,
            String path) {
    }
}
