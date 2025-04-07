package com.iqeq.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CustomException extends Exception {

    private final int status;
    private final String message;
    private final transient Object[] parameters;

    public CustomException() {
        this.status = 500;
        this.message = "Internal server exception";
        this.parameters = null;
    }

    public CustomException(int status, String message) {
        this.status = status;
        this.message = message;
        this.parameters = null;
    }

    public CustomException(int status, String message, Object[] parameters) {
        this.status = status;
        this.message = message;
        this.parameters = parameters;
    }
}
