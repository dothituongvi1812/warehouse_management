package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GoodsCreatedReceiptVoucherRequest {
    @NotBlank(message = "Mã hàng hoá không thể thiếu")
    private String codeGoods;
    @Min(value=1,message = "Số lượng hàng hoá phải lớn 0")
    private int quantity;

}
