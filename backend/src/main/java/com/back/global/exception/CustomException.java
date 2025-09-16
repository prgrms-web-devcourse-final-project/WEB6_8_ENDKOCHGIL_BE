package com.back.global.exception;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public String getMessageKey() {
        return errorCode.getMessage();
    }

    public org.springframework.http.HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
