package com.example.warehouse_management.payload.request.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRequest {
    @NotBlank(message = "Tên hàng hoá là không thể trống")
    private String name;
    @DecimalMin(value = "0.1",message = "Chiều cao phải lớn hơn 0")
    private double height;
    @DecimalMin(value = "0.1",message = "Chiều rộng phải lớn hơn 0")
    private double width;
    @DecimalMin(value = "0.1",message = "Chiều dài phải lớn hơn 0")
    private double length;
    @NotBlank(message = "Đơn vị hàng hoá không thể trống")
    private String unit;
    @Min(value = 1,message = "Số lượng hàng hoá phải lớn hơn 0")
    private int quantity;
    @NotBlank(message = "Loại hàng hoá là không thể trống")
    private String categoryCode;
    private double volume;
    private String image;

    public void setVolume(double width, double height, double length) {
        this.volume = width * height * length;
    }
    public double getVolume() {
        return height * width * length;
    }

}
