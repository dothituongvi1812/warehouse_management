package com.example.warehouse_management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodDeliveryRequest {
    @NotBlank(message = "Mã hàng hóa không thể trống")
    private String goodCode;
    @Min(value = 1,message = "Số lượng hàng hoá phải lớn hơn 0")
    private int quantity;
}
