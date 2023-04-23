package com.example.warehouse_management.payload.request.warehouse;

import com.example.warehouse_management.models.warehouse.Location;
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
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequest {
    @NotBlank(message = "Tên nhà kho là không thể trống")
    private String name;
    private Location location;
    @DecimalMin(value = "0.1",message = "Chiều dài của kho phải lớn hơn 0")
    private double length;
    @DecimalMin(value = "0.1",message = "Chiều rộng của kho phải lớn hơn 0")
    private double width;
    @DecimalMin(value = "0.1",message = "Chiều cao của kho phải lớn hơn 0")
    private double height;
    @DecimalMin(value = "0.1",message = "Chiều rộng của kệ phải lớn hơn 0")
    private double widthShelf;// 1m tối đa các kệ chỉ có 1 kích thước

    @DecimalMin(value = "0.1",message = "Chiều cao của kệ phải lớn hơn 0")
    private double heightShelf;//
    @DecimalMin(value = "0.1",message = "Chiều dài của kệ phải lớn hơn 0")
    private double lengthShelf;

    @Min(value = 1,message = "Số tầng của kệ phải lớn hơn 0")
    private int numberOfFloor;
    @DecimalMin(value = "0.1",message = "Chiều dài của cột phải lớn hơn 0")
    private double lengthOfColumn;

}
