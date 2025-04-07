package com.iqeq.exception;

import java.io.Serial;

public class AzureFileUploadException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public AzureFileUploadException(String msg, Exception cause) {
        super(msg,cause);
    }
}
