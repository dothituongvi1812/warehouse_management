package com.example.warehouse_management.payload.request.column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnLocationRequest {
    @DecimalMin(value = "0.1",message = "Chiều dài của cột phải lớn hơn 0")
    private double length;
    private String shelfStorageCode;
}
