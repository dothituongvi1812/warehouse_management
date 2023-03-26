package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ColumnLocationRequest {

    private double length;
    private String shelfStorageCode;
}
