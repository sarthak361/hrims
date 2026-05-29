package com.hti.exception;

import java.time.LocalDateTime;

public class ExceptionResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String requestUri;
    private String errorCode;

   
    public ExceptionResponse() {
    }

    public ExceptionResponse(String message, LocalDateTime timestamp, int status, String error,
            String requestUri, String errorCode) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.requestUri = requestUri;
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getRequestUri() { return requestUri; }
    public void setRequestUri(String requestUri) { this.requestUri = requestUri; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
}