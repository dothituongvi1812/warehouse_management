package com.example.warehouse_management.payload.response;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsStaticsResponse {
    private String name;
    private String code;
    private String categoryName;
    private Integer total;
}
