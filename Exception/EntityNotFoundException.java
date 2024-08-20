package com.ogn.orange.domain.Exception;

import lombok.Getter;

public class EntityNotFoundException extends RuntimeException{
    @Getter
    private ErreurCodes erreurCodes;
    public EntityNotFoundException(String message )
    {
        super(message);
    }
    public EntityNotFoundException(String message ,Throwable cause)
    {
        super(message, cause);
    }
    public EntityNotFoundException(String message ,Throwable cause,ErreurCodes erreurCodes)
    {
        super(message, cause);
        this.erreurCodes =erreurCodes;
    }
    public EntityNotFoundException(String message ,ErreurCodes erreurCodes)
    {
        super(message);
        this.erreurCodes =erreurCodes;
    }
}
