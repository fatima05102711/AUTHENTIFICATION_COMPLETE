package com.ogn.orange.domain.Dto;
import lombok.*;
@Builder
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private Integer statusCode;
}
