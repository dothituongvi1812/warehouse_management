package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class GoodsReceiptRequest {
    @NotBlank(message = "Mã hàng hoá không thể trống")
    private String goodsCode;
    @NotBlank(message = "Số lượng hàng hoá không thể trống")
    private Integer quantity;
}
