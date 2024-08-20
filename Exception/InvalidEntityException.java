package com.ogn.orange.domain.Exception;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class InvalidEntityException extends RuntimeException{
    private String message;
    private String errorCode;
    private String status;

}
