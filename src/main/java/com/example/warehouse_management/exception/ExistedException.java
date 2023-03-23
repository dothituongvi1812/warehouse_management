package com.example.warehouse_management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExistedException extends RuntimeException {
    private String message;
}
