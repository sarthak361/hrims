package com.hti.exception;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    private ResponseEntity<ExceptionResponse> buildErrorResponse(Exception ex, HttpStatus status,
            HttpServletRequest request, String errorCode) {
        LocalDateTime current = LocalDateTime.now();
        ExceptionResponse response = new ExceptionResponse(
                ex.getMessage(),
                toUtc(current),
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                errorCode
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception, HttpStatus.UNAUTHORIZED, request, "UNAUTHORIZED");
    }

  

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request, "BAD_REQUEST");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request, "NOT_FOUND");
    }



    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ExceptionResponse> handleInternalServerException(InternalServerException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, request, "INTERNAL_SERVER_ERROR");
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errormap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errormap.put(error.getField(), error.getDefaultMessage());
        });
        return errormap;
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds the allowed limit.");
    }

    // Generic fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, request, "INTERNAL_SERVER_ERROR");
    }

    private LocalDateTime toUtc(LocalDateTime current) {
        return current.atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}