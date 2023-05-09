package com.example.warehouse_management.payload.request.goods;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GoodsCheckedRequest {
    private String goodsCode;
    private int quantity;
}
