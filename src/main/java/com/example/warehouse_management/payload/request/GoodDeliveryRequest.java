package com.example.warehouse_management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodDeliveryRequest {
    private String goodCode;
    private int quantity;
}
