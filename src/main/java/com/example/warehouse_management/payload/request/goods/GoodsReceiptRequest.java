package com.example.warehouse_management.payload.request.goods;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class GoodsReceiptRequest {
    @NotBlank(message = "Mã hàng hoá không thể trống")
    private String goodsCode;
    @Min(value = 1,message = "Số lượng hàng hoá phải lớn hơn 0")
    private int quantity;
}
