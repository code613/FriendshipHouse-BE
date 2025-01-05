package com.levdevs.freindshipbe.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final String fieldName;
    private final String errorMessage;

    public ValidationException(String fieldName, String errorMessage) {
        super(errorMessage);
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

}
