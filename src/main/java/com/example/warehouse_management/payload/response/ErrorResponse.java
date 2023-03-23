package com.example.warehouse_management.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ErrorResponse {
    private int statusCode;
    private String message;
}
