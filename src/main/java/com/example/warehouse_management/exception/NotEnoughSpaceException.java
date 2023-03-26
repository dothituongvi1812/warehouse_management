package com.example.warehouse_management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotEnoughSpaceException extends RuntimeException{
    private String message;
}
