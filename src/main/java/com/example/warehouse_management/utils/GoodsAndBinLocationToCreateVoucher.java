package com.example.warehouse_management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsAndBinLocationToCreateVoucher {
    @NotBlank(message = "Mã hàng hoá không thể trống")
    private String  goodsCode;
    @Min(value = 1,message = "Số lượng hàng hoá phải lớn 0")
    private int quantity;
    @NotBlank(message = "Mã vị trí trên kệ không thể trống")
    private String binLocationCode;
}
