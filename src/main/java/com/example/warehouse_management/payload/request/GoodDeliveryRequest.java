package com.example.warehouse_management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodDeliveryRequest {
    private String goodCode;
    @Digits(message = "Quantity must be a number", integer = 8, fraction = 0)
    private int quantity;
}
