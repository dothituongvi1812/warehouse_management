package com.example.warehouse_management.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "Tên loại hàng hoá không thể trống")
    private String name;
    private String description;
}
