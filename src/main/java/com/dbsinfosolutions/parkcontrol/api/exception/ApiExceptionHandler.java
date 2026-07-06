package com.dbsinfosolutions.parkcontrol.api.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dbsinfosolutions.parkcontrol.domain.exception.BusinessException;
import com.dbsinfosolutions.parkcontrol.domain.exception.GarageFullException;
import com.dbsinfosolutions.parkcontrol.domain.exception.SessionNotFoundException;
import com.dbsinfosolutions.parkcontrol.domain.exception.SpotNotFoundException;

import jakarta.validation.ConstraintViolationException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_CONTENT, "Validation Error",
                exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_CONTENT, "Validation Error",
                exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", exception.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(GarageFullException.class)
    public ResponseEntity<ApiErrorResponse> handleGarageFull(GarageFullException exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Conflict", exception.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler({SessionNotFoundException.class, SpotNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(BusinessException exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", exception.getMessage(),
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
