package com.ogn.orange.domain.Exception;
import lombok.Getter;

public class InvalidOperationException extends RuntimeException{
    @Getter
    private ErreurCodes erreurCodes;
    public InvalidOperationException(String message) {
        super(message);
    }
    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidOperationException(String message, Throwable cause, ErreurCodes erreurCodes)
    {
        super(message, cause);
        this.erreurCodes = erreurCodes;
    }

    public InvalidOperationException(String message, ErreurCodes erreurCodes) {
        super(message);
        this.erreurCodes = erreurCodes;
    }
}
